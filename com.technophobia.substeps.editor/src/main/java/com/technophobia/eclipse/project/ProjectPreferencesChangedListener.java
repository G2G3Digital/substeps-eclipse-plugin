package com.technophobia.eclipse.project;

import org.eclipse.core.resources.IProject;

public interface ProjectPreferencesChangedListener {

    void preferencesChanged(IProject project);
}
