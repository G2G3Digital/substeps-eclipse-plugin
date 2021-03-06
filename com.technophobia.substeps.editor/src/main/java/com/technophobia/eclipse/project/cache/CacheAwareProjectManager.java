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
package com.technophobia.eclipse.project.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;

import com.technophobia.eclipse.project.ProjectChangedListener;
import com.technophobia.eclipse.project.ProjectEventType;
import com.technophobia.eclipse.project.ProjectFileChangedListener;
import com.technophobia.eclipse.project.ProjectObserver;
import com.technophobia.eclipse.project.cache.listener.ClassFileChangedListener;
import com.technophobia.eclipse.project.cache.listener.ClasspathChangedListener;
import com.technophobia.eclipse.project.cache.listener.FileWithExtensionChangedListener;
import com.technophobia.eclipse.project.cache.listener.ProjectCreatedListener;
import com.technophobia.eclipse.project.cache.listener.ProjectDeletedListener;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.observer.CacheMonitor;
import com.technophobia.substeps.supplier.Callback1;

public class CacheAwareProjectManager implements ProjectObserver {

    private final CacheMonitor<IProject>[] cacheMonitors;

    private final Collection<ProjectFileChangedListener> substepsChangeListeners;
    private final Collection<ProjectFileChangedListener> featureChangeListeners;
    private final Map<ProjectEventType, Set<ProjectChangedListener>> projectChangeListeners;

    private final Collection<IElementChangedListener> javaListeners;

    private final Collection<IResourceChangeListener> resourceChangeListeners;


    public CacheAwareProjectManager(final CacheMonitor<IProject>... cacheMonitors) {
        this.cacheMonitors = cacheMonitors;
        this.substepsChangeListeners = new HashSet<ProjectFileChangedListener>();
        this.featureChangeListeners = new HashSet<ProjectFileChangedListener>();
        this.projectChangeListeners = new HashMap<ProjectEventType, Set<ProjectChangedListener>>();
        this.resourceChangeListeners = new HashSet<IResourceChangeListener>();

        this.javaListeners = new ArrayList<IElementChangedListener>();
        this.javaListeners.add(createClassFilesChangedListener());
        this.javaListeners.add(createClasspathChangedListener());
        this.javaListeners.add(createProjectCreatedListener());
        this.javaListeners.add(createProjectDeletedListener());
        this.javaListeners.add(createCheckSubstepsCompatabilityOnProjectCreationListener());

        this.resourceChangeListeners.add(createSubstepsFileChangedListener());

    }


    @Override
    public void registerFrameworkListeners() {
        for (final IElementChangedListener listener : javaListeners) {
            JavaCore.addElementChangedListener(listener);
        }

        for (final IResourceChangeListener resourceChangeListener : resourceChangeListeners) {
            ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener,
                    IResourceChangeEvent.POST_CHANGE);
        }
    }


    @Override
    public void unregisterFrameworkListeners() {
        for (final IElementChangedListener listener : javaListeners) {
            JavaCore.removeElementChangedListener(listener);
        }

        for (final IResourceChangeListener resourceChangeListener : resourceChangeListeners) {
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
        }
    }


    @Override
    public void projectFileChange(final IProject project, final IFile file) {
        if (isSubstepsFile(file)) {
            FeatureEditorPlugin.instance().info(
                    "Substeps file " + file.getLocation() + " has changed in project " + project);
            updateCaches(project);
            updateFileChangeListeners(file, substepsChangeListeners);
        } else if (isFeatureFile(file)) {
            FeatureEditorPlugin.instance().info(
                    "Feature file " + file.getLocation() + " has changed in project " + project);
            updateCaches(project);
            updateFileChangeListeners(file, featureChangeListeners);
        }
    }


    @Override
    public void preferencesChanged(final IProject project) {
        FeatureEditorPlugin.instance().info("Preferences changed for project " + project);
        updateCaches(project);
    }


    @Override
    public void projectChanged(final IProject project) {
        updateCaches(project);
        for (final ProjectChangedListener listener : projectChangeListeners
                .get(ProjectEventType.ProjectConfigurationChanged)) {
            listener.projectChanged(project);
        }
    }


    @Override
    public void workspaceLoaded() {
        for (final IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
            updateCaches(project);
        }
    }


    @Override
    public void addFeatureFileListener(final ProjectFileChangedListener listener) {
        this.featureChangeListeners.add(listener);
    }


    @Override
    public void removeFeatureFileListener(final ProjectFileChangedListener listener) {
        this.featureChangeListeners.remove(listener);
    }


    @Override
    public void addSubstepsFileListener(final ProjectFileChangedListener listener) {
        this.substepsChangeListeners.add(listener);
    }


    @Override
    public void removeSubstepsFileListener(final ProjectFileChangedListener listener) {
        this.substepsChangeListeners.remove(listener);
    }


    @Override
    public void addProjectListener(final ProjectEventType projectEventType, final ProjectChangedListener listener) {
        if (!projectChangeListeners.containsKey(projectEventType)) {
            projectChangeListeners.put(projectEventType, new HashSet<ProjectChangedListener>());
        }
        this.projectChangeListeners.get(projectEventType).add(listener);
    }


    @Override
    public void removeProjectListener(final ProjectEventType projectEventType, final ProjectChangedListener listener) {
        if (projectChangeListeners.containsKey(projectEventType)) {
            projectChangeListeners.get(projectEventType).remove(listener);
        }
    }


    protected void updateCaches(final IProject project) {
        for (final CacheMonitor<IProject> cacheMonitor : cacheMonitors) {
            cacheMonitor.refreshCacheFor(project);
        }
    }


    protected void evictFromCaches(final IProject project) {
        for (final CacheMonitor<IProject> cacheMonitor : cacheMonitors) {
            cacheMonitor.evictFrom(project);
        }
    }


    protected IElementChangedListener createClassFilesChangedListener() {
        return new ClassFileChangedListener(updateProjectCachesCallback(ProjectEventType.SourceFileAnnotationsChanged));
    }


    protected IElementChangedListener createClasspathChangedListener() {
        return new ClasspathChangedListener(updateProjectCachesCallback(ProjectEventType.ProjectDependenciesChanged));
    }


    protected IElementChangedListener createProjectDeletedListener() {
        return new ProjectDeletedListener(removeProjectFromCacheCallback(ProjectEventType.ProjectRemoved));
    }


    protected IElementChangedListener createProjectCreatedListener() {
        return new ProjectCreatedListener(updateProjectCachesCallback(ProjectEventType.ProjectInserted));
    }


    private IResourceChangeListener createSubstepsFileChangedListener() {
        return new FileWithExtensionChangedListener(updateProjectCachesCallback(ProjectEventType.FeatureFileChanged),
                "feature", "substeps");
    }


    private IElementChangedListener createCheckSubstepsCompatabilityOnProjectCreationListener() {
        return new ProjectCreatedListener(new Callback1<IProject>() {
            @Override
            public void doCallback(final IProject t) {
                FeatureEditorPlugin.instance().checkSubstepsCompatibilityFor(t);
            }
        });
    }


    protected void notifyProjectChangeListeners(final IProject project, final ProjectEventType projectEventType) {
        if (projectChangeListeners.containsKey(projectEventType)) {
            for (final ProjectChangedListener listener : projectChangeListeners.get(projectEventType)) {
                listener.projectChanged(project);
            }
        }
    }


    private boolean isSubstepsFile(final IFile file) {
        return file.getFileExtension().toLowerCase().equals("substeps");
    }


    private boolean isFeatureFile(final IFile file) {
        return file.getFileExtension().toLowerCase().equals("feature");
    }


    private void updateFileChangeListeners(final IFile file, final Collection<ProjectFileChangedListener> listeners) {
        for (final ProjectFileChangedListener listener : listeners) {
            listener.projectFileChange(file.getProject(), file);
        }
    }


    private Callback1<IProject> updateProjectCachesCallback(final ProjectEventType projectEventType) {
        return new Callback1<IProject>() {
            @Override
            public void doCallback(final IProject project) {
                final Job job = new UpdateProjectInCachesJob("Update Substeps model for project " + project.getName(),
                        project, projectEventType);
                job.setRule(ResourcesPlugin.getWorkspace().getRoot());
                job.setPriority(Job.DECORATE);

                FeatureEditorPlugin.instance().info(
                        "Found " + Job.getJobManager().find(project).length + " existing jobs");
                Job.getJobManager().cancel(project);
                job.schedule(200);
            }
        };
    }


    private Callback1<IProject> removeProjectFromCacheCallback(final ProjectEventType projectEventType) {
        return new Callback1<IProject>() {
            @Override
            public void doCallback(final IProject project) {
                final Job job = new Job("Update Substeps model for project " + project.getName()) {

                    @Override
                    protected IStatus run(final IProgressMonitor monitor) {
                        evictFromCaches(project);
                        notifyProjectChangeListeners(project, projectEventType);
                        return Status.OK_STATUS;
                    }
                };
                job.setRule(ResourcesPlugin.getWorkspace().getRoot());
                job.setPriority(Job.DECORATE);
                job.schedule(200);
            }
        };
    }

    private final class UpdateProjectInCachesJob extends Job {
        private final IProject project;
        private final ProjectEventType projectEventType;


        private UpdateProjectInCachesJob(final String name, final IProject project,
                final ProjectEventType projectEventType) {
            super(name);
            this.project = project;
            this.projectEventType = projectEventType;
        }


        @Override
        protected IStatus run(final IProgressMonitor monitor) {
            notifyProjectChangeListeners(project, projectEventType);
            updateCaches(project);
            return Status.OK_STATUS;
        }


        @Override
        public boolean belongsTo(final Object family) {
            return project.equals(family);
        }
    }
}
