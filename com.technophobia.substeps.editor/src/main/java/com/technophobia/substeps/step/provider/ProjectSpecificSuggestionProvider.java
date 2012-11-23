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
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.eclipse.project.ProjectChangedListener;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.render.StepImplementationToSuggestionRenderer;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.step.Suggestion;
import com.technophobia.substeps.supplier.Transformer;

public class ProjectSpecificSuggestionProvider extends AbstractMultiProjectSuggestionProvider implements
        ProjectStepImplementationProvider, ProjectChangedListener {

    private final Transformer<IProject, Syntax> projectToSyntaxTransformer;
    private final StepImplementationToSuggestionRenderer stepRenderer;


    public ProjectSpecificSuggestionProvider(final Transformer<IProject, Syntax> projectToSyntaxTransformer,
            final StepImplementationToSuggestionRenderer stepRenderer) {
        super();
        this.projectToSyntaxTransformer = projectToSyntaxTransformer;
        this.stepRenderer = stepRenderer;
    }


    @Override
    public void load(final IWorkspace workspace) {
        super.load(workspace);
    }


    @Override
    public Collection<String> stepImplementationClasses(final IProject project) {
        clean(project);
        final Syntax syntax = projectToSyntaxTransformer.from(project);

        final List<StepImplementation> stepImplementations = syntax.getStepImplementations();

        final Collection<String> classes = new HashSet<String>();
        for (final StepImplementation stepImplementation : stepImplementations) {
            classes.add(stepImplementation.getImplementedIn().getName());
        }
        return classes;
    }


    @Override
    public void projectChanged(final IProject project) {
        markAsStale(project);
    }


    @Override
    protected Collection<Suggestion> findStepImplementationsFor(final IProject project) {
        final Syntax syntax = projectToSyntaxTransformer.from(project);

        final List<StepImplementation> stepImplementations = syntax.getStepImplementations();
        final Collection<Suggestion> suggestions = new ArrayList<Suggestion>(stepImplementations.size());
        for (final StepImplementation stepImplementation : stepImplementations) {
            suggestions.add(stepRenderer.render(stepImplementation));
        }

        return suggestions;
    }
}
