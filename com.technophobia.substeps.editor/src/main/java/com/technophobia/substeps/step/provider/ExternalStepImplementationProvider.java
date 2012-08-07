package com.technophobia.substeps.step.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Path;

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.step.ProjectSuggestionProvider;
import com.technophobia.substeps.supplier.Transformer;

public class ExternalStepImplementationProvider implements ProjectSuggestionProvider {

    private final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader;
    private final Map<IProject, List<StepImplementationsDescriptor>> stepImplementationMap;


    public ExternalStepImplementationProvider(
            final Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader) {
        this.stepImplementationLoader = stepImplementationLoader;
        this.stepImplementationMap = new HashMap<IProject, List<StepImplementationsDescriptor>>();
    }


    @Override
    public void load(final IWorkspace workspace) {
        for (final IProject project : workspace.getRoot().getProjects()) {
            loadProject(project);
        }

        workspace.addResourceChangeListener(new AddRemoveExternalStepImplementationsListener());
    }


    protected void loadProject(final IProject project) {
        if (!stepImplementationMap.containsKey(project)) {
            stepImplementationMap.put(project, new ArrayList<StepImplementationsDescriptor>());
        }

        stepImplementationMap.get(project).addAll(stepImplementationLoader.to(project));
    }


    protected void clearStepImplementationsFor(final IProject project) {
        stepImplementationMap.remove(project);
    }


    @Override
    public Collection<String> suggestionsFor(final IProject project) {
        final List<StepImplementationsDescriptor> stepImplementations = stepImplementationMap.containsKey(project) ? stepImplementationMap
                .get(project) : Collections.<StepImplementationsDescriptor> emptyList();

        final Collection<String> suggestions = new ArrayList<String>();
        for (final StepImplementationsDescriptor stepImplementation : stepImplementations) {
            for (final StepDescriptor step : stepImplementation.getExpressions()) {
                suggestions.add(step.getExpression());
            }
        }

        return Collections.unmodifiableCollection(suggestions);
    }

    private final class AddRemoveExternalStepImplementationsListener implements IResourceChangeListener {

        @Override
        public void resourceChanged(final IResourceChangeEvent event) {
            if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                final IResourceDelta[] projectDeltas = event.getDelta().getAffectedChildren();
                for (final IResourceDelta projectDelta : projectDeltas) {
                    final IProject project = toProject(projectDelta);
                    if (projectDelta.findMember(new Path(".classpath")) != null) {
                        clearStepImplementationsFor(project);
                        loadProject(project);
                    }
                }
            }
            System.out.println(event);
        }


        private IProject toProject(final IResourceDelta projectDelta) {
            final IResource resource = projectDelta.getResource();
            return (IProject) (resource instanceof IProject ? resource : null);
        }
    }
}
