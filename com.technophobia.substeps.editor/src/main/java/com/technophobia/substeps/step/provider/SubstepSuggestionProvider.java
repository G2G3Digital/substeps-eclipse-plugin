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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import com.technophobia.eclipse.project.ProjectChangedListener;
import com.technophobia.eclipse.project.ProjectFileChangedListener;
import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.Step;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.step.PatternSuggestion;
import com.technophobia.substeps.step.Suggestion;
import com.technophobia.substeps.supplier.Transformer;

public class SubstepSuggestionProvider extends AbstractMultiProjectSuggestionProvider implements
        ProjectFileChangedListener, ProjectChangedListener {

    private final Transformer<IProject, Syntax> projectToSyntaxTransformer;


    public SubstepSuggestionProvider(final Transformer<IProject, Syntax> projectToSyntaxTransformer) {
        this.projectToSyntaxTransformer = projectToSyntaxTransformer;
    }


    @Override
    public void projectFileChange(final IProject project, final IFile file) {
        markAsStale(project);
    }


    @Override
    public void projectChanged(final IProject project) {
        markAsStale(project);
    }


    @Override
    protected Collection<Suggestion> findStepImplementationsFor(final IProject project) {
        final Syntax syntax = projectToSyntaxTransformer.from(project);

        final List<ParentStep> substeps = syntax.getSortedRootSubSteps();
        final List<Suggestion> suggestions = new ArrayList<Suggestion>(substeps.size());
        for (final ParentStep substep : substeps) {
            final Step parentStep = substep.getParent();
            suggestions.add(new PatternSuggestion(parentStep.getPattern(), parentStep.getLine()));
        }
        return suggestions;
    }
}
