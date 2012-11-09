package com.technophobia.substeps.document.partition;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;

public class EditorInputPartitionContext implements PartitionContext {

    private final IEditorInput editorInput;


    public EditorInputPartitionContext(final IEditorInput editorInput) {
        this.editorInput = editorInput;
    }


    @Override
    public IProject currentProject() {
        final IResource resource = (IResource) editorInput.getAdapter(IResource.class);
        return resource.getProject();
    }

}
