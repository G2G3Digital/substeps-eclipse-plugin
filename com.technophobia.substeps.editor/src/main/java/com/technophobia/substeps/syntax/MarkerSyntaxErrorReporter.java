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

import com.technophobia.eclipse.preference.PreferenceLookup;
import com.technophobia.eclipse.transformer.FileToIFileTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.model.exception.StepImplementationException;
import com.technophobia.substeps.model.exception.SubstepsParsingException;
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
    public void reportFeatureError(final File file, final String line, final int lineNumber, final int offset,
            final String description) throws RuntimeException {
        addMarker(file, line, lineNumber, offset, description);
    }


    @Override
    public void reportFeatureError(final File file, final String line, final int lineNumber, final int offset,
            final String description, final RuntimeException ex) throws RuntimeException {
        addMarker(file, line, lineNumber, offset, description);
    }


    @Override
    public void reportSubstepsError(final SubstepsParsingException ex) throws RuntimeException {
        addMarker(ex.getFile(), ex.getLine(), ex.getLineNumber(), (int) ex.getOffset(), ex.getMessage());
    }


    @Override
    public void reportStepImplError(final StepImplementationException ex) {
        // TODO: Currently don't deal with errors in step impl classes
    }


    private void addMarker(final File file, final String line, final int lineNumber, final int offset,
            final String description) {
        final IFile f = new FileToIFileTransformer(project).from(file);

        if (!resourceToProblemMap.containsKey(f)) {
            resourceToProblemMap.put(f, new HashSet<Problem>());
        }
        resourceToProblemMap.get(f).add(Problem.createError(description, line, lineNumber, offset));
    }


    protected void clearErrorsFor(final IResource resource) {
        try {
            resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error("Could not clear problems for resource " + resource.getFullPath(), ex);
        }
    }
}
