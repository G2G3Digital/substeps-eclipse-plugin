package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchSite;

import com.technophobia.substeps.supplier.Transformer;

/**
 * Transformer that turns an {@link IWorkbenchSite} into an {@link IJavaProject}
 * based on the currently selected item. If nothing is selected, then whichever
 * project owns the current active editor is returned. Failing that, null is
 * returned
 * 
 * @author sforbes
 * 
 */
public class SiteToJavaProjectTransformer implements Transformer<IWorkbenchSite, IJavaProject> {

    private final Transformer<IProject, IJavaProject> projectToJavaProjectTransformer;


    public SiteToJavaProjectTransformer() {
        this(new ProjectToJavaProjectTransformer());
    }


    public SiteToJavaProjectTransformer(final Transformer<IProject, IJavaProject> projectToJavaProjectTransformer) {
        this.projectToJavaProjectTransformer = projectToJavaProjectTransformer;
    }


    @Override
    public IJavaProject from(final IWorkbenchSite site) {
        final IProject project = projectFor(site);
        if (project != null) {
            return projectToJavaProjectTransformer.from(project);
        }
        return null;
    }


    /**
     * Find the current project
     * 
     * @param site
     *            The site
     * @return Current project
     */
    private IProject projectFor(final IWorkbenchSite site) {
        final IProject selectedProject = selectedProject(site);
        if (selectedProject != null) {
            return selectedProject;
        }
        return currentEditedProject(site);
    }


    /**
     * Returns project based on the selection service
     * 
     * @param site
     *            The site
     * @return Selected project, or null if nothing is selected
     */
    private IProject selectedProject(final IWorkbenchSite site) {
        final ISelection selection = site.getWorkbenchWindow().getSelectionService().getSelection();
        if (selection != null && selection instanceof IStructuredSelection) {
            final Object element = ((IStructuredSelection) selection).getFirstElement();
            if (element instanceof IResource) {
                return ((IResource) element).getProject();
            } else if (element instanceof IAdaptable) {
                final IAdaptable adaptable = (IAdaptable) element;
                final IResource resource = (IResource) adaptable.getAdapter(IResource.class);
                return resource.getProject();
            }
        }
        return null;
    }


    /**
     * Returns project based on the currently active editor
     * 
     * @param site
     *            The site
     * @return Selected project, or null if nothing is currently being edited
     */
    private IProject currentEditedProject(final IWorkbenchSite site) {
        final IEditorPart activeEditor = site.getPage().getActiveEditor();
        if (activeEditor != null) {
            final IFile file = (IFile) activeEditor.getEditorInput().getAdapter(IFile.class);
            return file.getProject();
        }
        return null;
    }
}
