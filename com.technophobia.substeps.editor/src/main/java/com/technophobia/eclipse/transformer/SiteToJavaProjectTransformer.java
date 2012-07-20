package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchSite;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Transformer;

public class SiteToJavaProjectTransformer implements Transformer<IWorkbenchSite, IJavaProject> {

    @Override
    public IJavaProject to(final IWorkbenchSite site) {
        final IProject project = projectFor(site);
        if (project != null) {
            try {
                if (project.hasNature(JavaCore.NATURE_ID)) {
                    return JavaCore.create(project);
                } else {
                    warn("Could not transform project " + project.getName() + " to a java project");
                }
            } catch (final CoreException e) {
                warn("Could not transform project " + project.getName() + " to java project: " + e.getMessage());
            }
        }
        return null;
    }


    private IProject projectFor(final IWorkbenchSite site) {
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

        final IEditorPart activeEditor = site.getPage().getActiveEditor();
        if (activeEditor != null) {
            final IFile file = (IFile) activeEditor.getEditorInput().getAdapter(IFile.class);
            return file.getProject();
        }
        return null;
    }


    private void warn(final String message) {
        FeatureEditorPlugin.log(Status.WARNING, message);
    }

}
