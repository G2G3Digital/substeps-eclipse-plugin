package com.technophobia.eclipse.project;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.supplier.Callback1;

public interface ProjectManager {

    void registerProjectListeners();


    void projectFileChanged(IProject project, IFile file);


    void addFeatureFileListener(Callback1<IFile> listener);


    void removeFeatureFileListener(Callback1<IFile> listener);


    void addSubstepsFileListener(Callback1<IFile> listener);


    void removeSubstepsFileListener(Callback1<IFile> listener);
}
