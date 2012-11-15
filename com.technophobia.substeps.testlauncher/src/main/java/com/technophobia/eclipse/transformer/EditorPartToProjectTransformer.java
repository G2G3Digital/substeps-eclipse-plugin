package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorPart;

import com.technophobia.substeps.supplier.Transformer;

public class EditorPartToProjectTransformer implements Transformer<IEditorPart, IProject> {

    @Override
    public IProject from(final IEditorPart editor) {
        final IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
        final IProject project = resource.getProject();
        return project;
    }

}
