package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;

import com.technophobia.substeps.step.ProjectSuggestionProvider;

public abstract class AbstractMultiProjectSuggestionProvider implements ProjectSuggestionProvider {

    private final Map<IProject, List<String>> stepImplementationMap;


    public AbstractMultiProjectSuggestionProvider() {
        this.stepImplementationMap = new HashMap<IProject, List<String>>();
    }


    @Override
    public void load(final IWorkspace workspace) {
        for (final IProject project : workspace.getRoot().getProjects()) {
            loadProject(project);
        }
    }


    protected void loadProject(final IProject project) {
        if (!stepImplementationMap.containsKey(project)) {
            stepImplementationMap.put(project, new ArrayList<String>());
        }

        stepImplementationMap.get(project).addAll(findStepImplementationsFor(project));
    }


    protected void clearStepImplementationsFor(final IProject project) {
        stepImplementationMap.remove(project);
    }


    @Override
    public Collection<String> suggestionsFor(final IProject project) {
        return stepImplementationMap.containsKey(project) ? stepImplementationMap.get(project) : Collections
                .<String> emptyList();

    }


    protected abstract Collection<String> findStepImplementationsFor(IProject project);
}
