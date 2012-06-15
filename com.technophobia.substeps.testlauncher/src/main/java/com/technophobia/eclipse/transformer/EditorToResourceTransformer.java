package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorPart;

public class EditorToResourceTransformer implements Transformer<org.eclipse.ui.IEditorPart, IResource> {

    @Override
    public IResource to(final IEditorPart editor) {
        return (IResource) editor.getEditorInput().getAdapter(IResource.class);
    }

}
