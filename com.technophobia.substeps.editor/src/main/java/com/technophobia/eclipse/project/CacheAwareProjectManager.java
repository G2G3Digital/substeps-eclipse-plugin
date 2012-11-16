package com.technophobia.eclipse.project;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.observer.CacheMonitor;
import com.technophobia.substeps.supplier.Callback1;

public class CacheAwareProjectManager implements ProjectManager {

    private final CacheMonitor<IProject>[] cacheMonitors;

    private final Collection<Callback1<IFile>> substepsChangeListeners;
    private final Collection<Callback1<IFile>> featureChangeListeners;


    public CacheAwareProjectManager(final CacheMonitor<IProject>... cacheMonitors) {
        this.cacheMonitors = cacheMonitors;
        this.substepsChangeListeners = new HashSet<Callback1<IFile>>();
        this.featureChangeListeners = new HashSet<Callback1<IFile>>();
    }


    @Override
    public void registerProjectListeners() {
        registerResourceChangedListener();
        registerClasspathListener();
    }


    @Override
    public void projectFileChanged(final IProject project, final IFile file) {
        if (isSubstepsFile(file)) {
            updateCaches(project);
            updateSubstepsChangeListeners(file);
        } else if (isFeatureFile(file)) {
            updateCaches(project);
            updateFeatureChangeListeners(file);
        }
    }


    @Override
    public void addFeatureFileListener(final Callback1<IFile> listener) {
        this.featureChangeListeners.add(listener);
    }


    @Override
    public void removeFeatureFileListener(final Callback1<IFile> listener) {
        this.featureChangeListeners.remove(listener);
    }


    @Override
    public void addSubstepsFileListener(final Callback1<IFile> listener) {
        this.substepsChangeListeners.add(listener);
    }


    @Override
    public void removeSubstepsFileListener(final Callback1<IFile> listener) {
        this.substepsChangeListeners.remove(listener);
    }


    protected void updateCaches(final IProject project) {
        for (final CacheMonitor<IProject> cacheMonitor : cacheMonitors) {
            cacheMonitor.refreshCacheFor(project);
        }
    }


    private boolean isSubstepsFile(final IFile file) {
        return file.getFileExtension().toLowerCase().equals("substeps");
    }


    private boolean isFeatureFile(final IFile file) {
        return file.getFileExtension().toLowerCase().equals("feature");
    }


    private void updateFeatureChangeListeners(final IFile file) {
        for (final Callback1<IFile> callback : featureChangeListeners) {
            callback.doCallback(file);
        }
    }


    private void updateSubstepsChangeListeners(final IFile file) {
        for (final Callback1<IFile> callback : substepsChangeListeners) {
            callback.doCallback(file);
        }
    }


    private Set<IProject> findProjectsInChangeset(final IResourceChangeEvent event) {
        final Set<IProject> changedProjects = new HashSet<IProject>();
        final IResourceDelta[] projectDeltas = event.getDelta().getAffectedChildren();
        for (final IResourceDelta projectDelta : projectDeltas) {
            final IProject project = toProject(projectDelta);
            changedProjects.add(project);
        }
        return changedProjects;
    }


    private void registerResourceChangedListener() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
            @Override
            public void resourceChanged(final IResourceChangeEvent event) {
                if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                    final IResourceDelta rootDelta = event.getDelta();

                    final Collection<IProject> changedProjects = new HashSet<IProject>();
                    final IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {

                        @Override
                        public boolean visit(final IResourceDelta delta) throws CoreException {
                            if (delta.getKind() == IResourceDelta.CHANGED) {
                                if ((delta.getFlags() & IResourceDelta.CONTENT) == IResourceDelta.CONTENT) {
                                    if ("java".equals(delta.getResource().getFileExtension().toLowerCase())) {
                                        changedProjects.add(delta.getResource().getProject());
                                    }
                                }
                            }
                            return true;
                        }
                    };

                    try {
                        rootDelta.accept(visitor);
                    } catch (final CoreException e) {
                        FeatureEditorPlugin.instance().error(
                                "Could not determine changed resources for root delta "
                                        + rootDelta.getResource().getLocation());
                        return;
                    }

                    for (final IProject project : changedProjects) {
                        updateCaches(project);
                    }
                }
            }
        });
    }


    private void registerClasspathListener() {
        JavaCore.addElementChangedListener(new IElementChangedListener() {

            @Override
            public void elementChanged(final ElementChangedEvent event) {
                final IJavaElementDelta delta = event.getDelta();

                if (isClasspathChange(delta)) {
                    try {
                        for (final IJavaProject javaProject : delta.getElement().getJavaModel().getJavaProjects()) {
                            updateCaches(javaProject.getProject());
                        }
                    } catch (final JavaModelException ex) {
                        FeatureEditorPlugin.instance().error(
                                "Could not update caches for classpath change with event " + event, ex);
                    }
                }
            }


            private boolean isClasspathChange(final IJavaElementDelta delta) {
                final int flags = delta.getFlags();
                if (hasFlag(IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED, flags)
                        || hasFlag(IJavaElementDelta.F_ADDED_TO_CLASSPATH, flags)
                        || hasFlag(IJavaElementDelta.F_REMOVED_FROM_CLASSPATH, flags)) {
                    return true;
                }
                for (final IJavaElementDelta child : delta.getAffectedChildren()) {
                    if (isClasspathChange(child)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }


    protected boolean hasFlag(final int flag, final int flags) {
        return (flags & flag) == flag;
    }


    private IProject toProject(final IResourceDelta projectDelta) {
        final IResource resource = projectDelta.getResource();
        return (IProject) (resource instanceof IProject ? resource : null);
    }
}
