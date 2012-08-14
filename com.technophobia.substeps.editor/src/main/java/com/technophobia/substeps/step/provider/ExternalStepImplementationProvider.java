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

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.supplier.Transformer;

public class ExternalStepImplementationProvider extends AbstractMultiProjectSuggestionProvider implements
        ProjectStepImplementationProvider {

    private final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader;
    private final Map<IProject, Set<String>> externalStepClassesForProject;


    public ExternalStepImplementationProvider(
            final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader) {
        super();
        this.stepImplementationLoader = stepImplementationLoader;
        this.externalStepClassesForProject = new HashMap<IProject, Set<String>>();
    }


    @Override
    public void load(final IWorkspace workspace) {
        super.load(workspace);

        workspace.addResourceChangeListener(new AddRemoveExternalStepImplementationsListener(this));
    }


    @Override
    protected void clearStepImplementationsFor(final IProject project) {
        super.clearStepImplementationsFor(project);
        this.externalStepClassesForProject.remove(project);
    }


    @Override
    public Collection<String> stepImplementationClasses(final IProject project) {

        return externalStepClassesForProject.containsKey(project) ? externalStepClassesForProject.get(project)
                : Collections.<String> emptyList();
    }


    @Override
    protected Collection<String> findStepImplementationsFor(final IProject project) {

        final List<StepImplementationsDescriptor> stepImplementations = stepImplementationLoader.from(project);
        final Set<String> stepImplementationClasses = new HashSet<String>();

        final Collection<String> suggestions = new ArrayList<String>();
        for (final StepImplementationsDescriptor stepImplementation : stepImplementations) {
            stepImplementationClasses.add(stepImplementation.getClassName());
            for (final StepDescriptor step : stepImplementation.getExpressions()) {
                suggestions.add(step.getExpression());
            }
        }

        externalStepClassesForProject.put(project, stepImplementationClasses);

        return suggestions;
    }
}
