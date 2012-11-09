package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.technophobia.substeps.supplier.Transformer;

public class SelectionToProjectTransformer implements Transformer<ISelection, IProject> {

    @Override
    public IProject from(final ISelection selection) {
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
}
