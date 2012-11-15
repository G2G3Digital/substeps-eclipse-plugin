package com.technophobia.substeps.step;

import java.util.ArrayList;
import java.util.Collection;
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


    public void load(final IWorkspace workspace) {
        for (final Set<ProjectSuggestionProvider> providers : projectSuggestionProviders.values()) {
            for (final ProjectSuggestionProvider provider : providers) {
                provider.load(workspace);
            }
        }
    }


    @Override
    public List<String> suggestionsFor(final SuggestionType suggestionType, final IResource resource) {

        final Set<String> suggestions = new HashSet<String>();

        final IProject project = resourceToProjectTransformer.from(resource);

        for (final Entry<SuggestionSource, Set<ProjectSuggestionProvider>> entry : projectSuggestionProviders
                .entrySet()) {
            if (suggestionType.isPermittedSuggestionSource(entry.getKey())) {
                for (final ProjectSuggestionProvider provider : entry.getValue()) {
                    suggestions.addAll(provider.suggestionsFor(project));
                }
            }
        }
        return new ArrayList<String>(suggestions);
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
