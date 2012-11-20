package com.technophobia.substeps.syntax;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.technophobia.eclipse.lookup.PreferenceLookup;
import com.technophobia.eclipse.transformer.FileToIFileTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.preferences.SubstepsPreferences;

public class MarkerSyntaxErrorReporter implements DeferredReportingSyntaxErrorReporter {

    private final IProject project;
    private final Map<IResource, Set<Problem>> resourceToProblemMap;
    private final PreferenceLookup preferenceLookup;


    public MarkerSyntaxErrorReporter(final IProject project, final PreferenceLookup preferenceLookup) {
        this.project = project;
        this.preferenceLookup = preferenceLookup;
        this.resourceToProblemMap = new HashMap<IResource, Set<Problem>>();
    }


    @Override
    public void applyChanges() {
        final Job job = new Job("Update problems view") {

            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                clearErrorsFor(project);

                if (preferenceLookup.booleanFor(SubstepsPreferences.ENABLE_PROBLEMS.key())) {
                    for (final Map.Entry<IResource, Set<Problem>> entry : resourceToProblemMap.entrySet()) {
                        final IResource resource = entry.getKey();
                        final Set<Problem> problems = entry.getValue();
                        for (final Problem problem : problems) {
                            problem.mark(resource);
                        }
                    }
                }
                return Status.OK_STATUS;
            }
        };
        job.setRule(ResourcesPlugin.getWorkspace().getRoot());
        job.schedule();
    }


    @Override
    public void reportFeatureError(final File file, final String line, final int lineNumber, final String description)
            throws RuntimeException {
        addMarker(file, line, lineNumber, description);
    }


    @Override
    public void reportFeatureError(final File file, final String line, final int lineNumber, final String description,
            final RuntimeException ex) throws RuntimeException {
        addMarker(file, line, lineNumber, description);
    }


    @Override
    public void reportSubstepsError(final File file, final String line, final int lineNumber, final String description)
            throws RuntimeException {
        addMarker(file, line, lineNumber, description);
    }


    @Override
    public void reportSubstepsError(final File file, final String line, final int lineNumber, final String description,
            final RuntimeException ex) throws RuntimeException {
        addMarker(file, line, lineNumber, description);
    }


    private void addMarker(final File file, final String line, final int lineNumber, final String description) {
        final IFile f = new FileToIFileTransformer(project).from(file);

        if (!resourceToProblemMap.containsKey(f)) {
            resourceToProblemMap.put(f, new HashSet<Problem>());
        }
        resourceToProblemMap.get(f).add(Problem.createError(description, line, lineNumber));
    }


    protected void clearErrorsFor(final IResource resource) {
        try {
            resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not clear problems for resource " + resource.getFullPath(), ex);
        }
    }
}
