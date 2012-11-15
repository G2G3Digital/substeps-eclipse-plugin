package com.technophobia.substeps.document.partition;

import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.step.ContextualSuggestionManager;

public interface PartitionContext {

    IProject currentProject();


    ContextualSuggestionManager suggestionManager();
}
