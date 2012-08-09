package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.technophobia.substeps.supplier.Transformer;

public class SelectionToFileOrNullTransformer implements Transformer<ISelection, IFile> {

    @Override
    public IFile from(final ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (structuredSelection.size() == 1) {
                final Object element = structuredSelection.getFirstElement();
                if (element instanceof IFile) {
                    return (IFile) element;
                }
            }
        }
        return null;
    }

}
