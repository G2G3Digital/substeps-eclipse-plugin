package com.technophobia.substeps.step.provider;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Path;

public class AddRemoveExternalStepImplementationsListener implements IResourceChangeListener {

    private final ExternalStepImplementationProvider suggestionProvider;


    public AddRemoveExternalStepImplementationsListener(final ExternalStepImplementationProvider suggestionProvider) {
        this.suggestionProvider = suggestionProvider;
    }


    @Override
    public void resourceChanged(final IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
            final IResourceDelta[] projectDeltas = event.getDelta().getAffectedChildren();
            for (final IResourceDelta projectDelta : projectDeltas) {
                final IProject project = toProject(projectDelta);
                if (projectDelta.findMember(new Path(".classpath")) != null) {
                    suggestionProvider.clearStepImplementationsFor(project);
                    suggestionProvider.loadProject(project);
                }
            }
        }
    }


    private IProject toProject(final IResourceDelta projectDelta) {
        final IResource resource = projectDelta.getResource();
        return (IProject) (resource instanceof IProject ? resource : null);
    }
}
