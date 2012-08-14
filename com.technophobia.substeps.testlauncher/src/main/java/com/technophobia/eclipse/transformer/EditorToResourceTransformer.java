package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorPart;

import com.technophobia.substeps.supplier.Transformer;

public class EditorToResourceTransformer implements Transformer<org.eclipse.ui.IEditorPart, IResource> {

    @Override
    public IResource from(final IEditorPart editor) {
        return (IResource) editor.getEditorInput().getAdapter(IResource.class);
    }

}
