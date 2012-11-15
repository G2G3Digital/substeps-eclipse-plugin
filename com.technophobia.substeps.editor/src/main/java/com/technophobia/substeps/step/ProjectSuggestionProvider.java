package com.technophobia.substeps.step;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;

public interface ProjectSuggestionProvider {

    Collection<String> suggestionsFor(IProject project);


    void load(IWorkspace workspace);
}
