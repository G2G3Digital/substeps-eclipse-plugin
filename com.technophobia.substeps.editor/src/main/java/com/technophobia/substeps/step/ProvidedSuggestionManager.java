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
package com.technophobia.substeps.step;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.substeps.supplier.Transformer;

public class ProvidedSuggestionManager implements ContextualSuggestionManager, ProjectStepImplementationProvider {

    private final Transformer<IResource, IProject> resourceToProjectTransformer;
    private final Map<SuggestionSource, Set<ProjectSuggestionProvider>> projectSuggestionProviders;


    public ProvidedSuggestionManager(final Transformer<IResource, IProject> resourceToProjectTransformer) {
        this.resourceToProjectTransformer = resourceToProjectTransformer;
        this.projectSuggestionProviders = new HashMap<SuggestionSource, Set<ProjectSuggestionProvider>>();
    }


    public void addProvider(final SuggestionSource source, final ProjectSuggestionProvider provider) {
        if (!projectSuggestionProviders.containsKey(source)) {
            projectSuggestionProviders.put(source, new HashSet<ProjectSuggestionProvider>());
        }
        projectSuggestionProviders.get(source).add(provider);
    }


    public Collection<ProjectSuggestionProvider> providersOfSource(final SuggestionSource source) {
        return projectSuggestionProviders.get(source);
    }


    public void load(final IWorkspace workspace) {
        for (final Set<ProjectSuggestionProvider> providers : projectSuggestionProviders.values()) {
            for (final ProjectSuggestionProvider provider : providers) {
                provider.load(workspace);
            }
        }
    }


    @Override
    public List<Suggestion> suggestionsFor(final IResource resource) {

        final Set<Suggestion> suggestions = new HashSet<Suggestion>();

        final IProject project = resourceToProjectTransformer.from(resource);

        for (final Entry<SuggestionSource, Set<ProjectSuggestionProvider>> entry : projectSuggestionProviders
                .entrySet()) {
            for (final ProjectSuggestionProvider provider : entry.getValue()) {
                suggestions.addAll(provider.suggestionsFor(project));
            }
        }
        final List<Suggestion> suggestionsList = new ArrayList<Suggestion>(suggestions);
        Collections.sort(suggestionsList);
        return suggestionsList;
    }


    @Override
    public Collection<String> stepImplementationClasses(final IProject project) {
        final Collection<String> classes = new HashSet<String>();
        for (final Set<ProjectSuggestionProvider> providers : projectSuggestionProviders.values()) {
            for (final ProjectSuggestionProvider provider : providers) {
                if (provider instanceof ProjectStepImplementationProvider) {
                    classes.addAll(((ProjectStepImplementationProvider) provider).stepImplementationClasses(project));
                }
            }
        }
        return classes;
    }
}
