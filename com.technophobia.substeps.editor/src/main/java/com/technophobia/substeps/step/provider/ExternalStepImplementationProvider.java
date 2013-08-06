/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.eclipse.project.ProjectChangedListener;
import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.step.PatternSuggestion;
import com.technophobia.substeps.step.ProjectStepDescriptorProvider;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.step.Suggestion;
import com.technophobia.substeps.supplier.Transformer;

public class ExternalStepImplementationProvider extends AbstractMultiProjectSuggestionProvider implements
        ProjectStepImplementationProvider, ProjectStepDescriptorProvider, ProjectChangedListener {

    private static final String REGEX = "([^\"]*)";

    private final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader;
    private final Map<IProject, Collection<StepImplementationsDescriptor>> externalStepDescriptorsForProject;


    public ExternalStepImplementationProvider(
            final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader) {
        super();
        this.stepImplementationLoader = stepImplementationLoader;
        this.externalStepDescriptorsForProject = new HashMap<IProject, Collection<StepImplementationsDescriptor>>();
    }


    @Override
    public void load(final IWorkspace workspace) {
        super.load(workspace);
    }


    @Override
    public Collection<String> stepImplementationClasses(final IProject project) {
        clean(project);
        return externalStepDescriptorsForProject.containsKey(project) ? asStepClasses(externalStepDescriptorsForProject
                .get(project)) : Collections.<String> emptyList();
    }


    @Override
    public List<StepDescriptor> stepDescriptorsFor(final IProject project, final String className) {
        return externalStepDescriptorsForProject.containsKey(project) ? stepDescriptorsFor(className,
                externalStepDescriptorsForProject.get(project)) : Collections.<StepDescriptor> emptyList();
    }


    @Override
    public void projectChanged(final IProject project) {
        markAsStale(project);
    }


    @Override
    protected void clearStepImplementationsFor(final IProject project) {
        super.clearStepImplementationsFor(project);
        this.externalStepDescriptorsForProject.remove(project);
    }


    @Override
    protected Collection<Suggestion> findStepImplementationsFor(final IProject project) {

        final List<StepImplementationsDescriptor> stepImplementations = stepImplementationLoader.from(project);
        final Set<String> stepImplementationClasses = new HashSet<String>();
        final Collection<Suggestion> suggestions = new ArrayList<Suggestion>();
        for (final StepImplementationsDescriptor stepImplementation : stepImplementations) {
            stepImplementationClasses.add(stepImplementation.getClassName());
            for (final StepDescriptor step : stepImplementation.getExpressions()) {
                if (isPattern(step)) {
                    suggestions.add(findPatternIn(step));
                } else {
                    suggestions.add(new Suggestion(step.getExpression()));
                }
            }
        }

        externalStepDescriptorsForProject.put(project, stepImplementations);

        return suggestions;
    }


    private boolean isPattern(final StepDescriptor step) {
        final String expression = step.getExpression();
        final String example = step.getExample();

        return example != null && !example.equals(expression);
    }


    private Suggestion findPatternIn(final StepDescriptor step) {

        String currentExpression = step.getExpression();
        final StringBuilder sb = new StringBuilder();

        int startChevronIndex = currentExpression.indexOf('<');
        while (startChevronIndex > -1) {
            final int endChevronIndex = currentExpression.indexOf('>');
            if (endChevronIndex > startChevronIndex) {
                sb.append(currentExpression.substring(0, startChevronIndex));
                sb.append(REGEX);
                currentExpression = currentExpression.substring(endChevronIndex + 1);
                startChevronIndex = currentExpression.indexOf('<');
            }
        }
        sb.append(currentExpression);

        return new PatternSuggestion(sb.toString(), step.getExpression());
    }


    private List<String> asStepClasses(final Collection<StepImplementationsDescriptor> descriptors) {
        final List<String> stepClasses = new ArrayList<String>(descriptors.size());
        for (final StepImplementationsDescriptor descriptor : descriptors) {
            stepClasses.add(descriptor.getClassName());
        }
        return Collections.unmodifiableList(stepClasses);
    }


    private List<StepDescriptor> stepDescriptorsFor(final String className,
            final Collection<StepImplementationsDescriptor> stepImplementationDescriptors) {
        for (final StepImplementationsDescriptor stepImplementationDescriptor : stepImplementationDescriptors) {
            if (className.equals(stepImplementationDescriptor.getClassName())) {
                return stepImplementationDescriptor.getExpressions();
            }
        }
        return Collections.emptyList();
    }
}
