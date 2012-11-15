package com.technophobia.substeps.step;

import java.util.List;

import org.eclipse.core.resources.IResource;

public interface ContextualSuggestionManager {

    List<String> suggestionsFor(IResource resource);
}
