package com.technophobia.substeps.document.partition;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;

import com.technophobia.substeps.step.ContextualSuggestionManager;

public class EditorInputPartitionContext implements PartitionContext {

    private final IEditorInput editorInput;
    private final ContextualSuggestionManager suggestionManager;


    public EditorInputPartitionContext(final IEditorInput editorInput,
            final ContextualSuggestionManager suggestionManager) {
        this.editorInput = editorInput;
        this.suggestionManager = suggestionManager;
    }


    @Override
    public IProject currentProject() {
        final IResource resource = (IResource) editorInput.getAdapter(IResource.class);
        return resource.getProject();
    }


    @Override
    public ContextualSuggestionManager suggestionManager() {
        return suggestionManager;
    }
}
