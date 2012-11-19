package com.technophobia.eclipse.project;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public interface ProjectFileChangedListener {

    void projectFileChange(IProject project, IFile file);
}
