package com.technophobia.eclipse.project;

public interface ProjectManager extends ProjectFileChangedListener, ProjectPreferencesChangedListener {

    void registerFrameworkListeners();


    void unregisterFrameworkListeners();


    void addFeatureFileListener(ProjectFileChangedListener listener);


    void removeFeatureFileListener(ProjectFileChangedListener listener);


    void addSubstepsFileListener(ProjectFileChangedListener listener);


    void removeSubstepsFileListener(ProjectFileChangedListener listener);


    void addProjectListener(ProjectEventType projectEventType, ProjectChangedListener listener);


    void removeProjectListener(ProjectEventType projectEventType, ProjectChangedListener listener);
}
