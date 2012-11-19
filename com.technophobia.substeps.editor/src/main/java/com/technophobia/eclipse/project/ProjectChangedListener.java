package com.technophobia.eclipse.project;

import org.eclipse.core.resources.IProject;

public interface ProjectChangedListener {

    void projectChanged(IProject project);
}
