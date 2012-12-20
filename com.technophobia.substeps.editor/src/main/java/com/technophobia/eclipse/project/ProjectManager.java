package com.technophobia.eclipse.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

public interface ProjectManager {

    IPath featureFolderFor(IProject project);


    IPath substepsFolderFor(IProject project);
}
