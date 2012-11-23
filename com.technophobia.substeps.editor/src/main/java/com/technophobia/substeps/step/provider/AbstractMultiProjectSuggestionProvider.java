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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.step.ProjectSuggestionProvider;
import com.technophobia.substeps.step.Suggestion;

public abstract class AbstractMultiProjectSuggestionProvider implements ProjectSuggestionProvider {

    private final Map<IProject, List<Suggestion>> stepImplementationMap;

    private final Set<IProject> staleProjects;


    public AbstractMultiProjectSuggestionProvider() {
        this.stepImplementationMap = new HashMap<IProject, List<Suggestion>>();
        this.staleProjects = new HashSet<IProject>();
    }


    @Override
    public void load(final IWorkspace workspace) {
        for (final IProject project : workspace.getRoot().getProjects()) {
            if (project.isAccessible() && isJavaProject(project)) {
                loadProject(project);
            }
        }
    }


    private boolean isJavaProject(final IProject project) {
        try {
            return project.hasNature(JavaCore.NATURE_ID);
        } catch (final CoreException e) {
            FeatureEditorPlugin.instance().warn(
                    "Could not determine if project " + project.getName() + " is a java project, returning false");
            return false;
        }
    }


    protected void loadProject(final IProject project) {
        if (!stepImplementationMap.containsKey(project)) {
            stepImplementationMap.put(project, new ArrayList<Suggestion>());
        } else {
            stepImplementationMap.get(project).clear();
        }

        stepImplementationMap.get(project).addAll(findStepImplementationsFor(project));
    }


    protected void clearStepImplementationsFor(final IProject project) {
        stepImplementationMap.remove(project);
    }


    @Override
    public Collection<Suggestion> suggestionsFor(final IProject project) {
        clean(project);
        return stepImplementationMap.containsKey(project) ? stepImplementationMap.get(project) : Collections
                .<Suggestion> emptyList();

    }


    protected void markAsStale(final IProject project) {
        staleProjects.add(project);
        clearStepImplementationsFor(project);
    }


    protected void clean(final IProject project) {
        if (staleProjects.contains(project) || isEmpty(project)) {
            loadProject(project);
            staleProjects.remove(project);
        }
    }


    protected boolean isEmpty(final IProject project) {
        return !stepImplementationMap.containsKey(project);
    }


    protected abstract Collection<Suggestion> findStepImplementationsFor(IProject project);
}
