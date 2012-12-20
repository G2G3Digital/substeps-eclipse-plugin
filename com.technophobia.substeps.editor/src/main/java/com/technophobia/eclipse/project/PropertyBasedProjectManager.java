package com.technophobia.eclipse.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.technophobia.eclipse.preference.PreferenceLookupFactory;
import com.technophobia.substeps.preferences.SubstepsPreferences;

public class PropertyBasedProjectManager implements ProjectManager {

    private final PreferenceLookupFactory<IProject> preferenceLookupFactory;


    public PropertyBasedProjectManager(final PreferenceLookupFactory<IProject> preferenceLookup) {
        this.preferenceLookupFactory = preferenceLookup;
    }


    @Override
    public IPath featureFolderFor(final IProject project) {
        return folderFor(SubstepsPreferences.FEATURE_FOLDER, project);
    }


    @Override
    public IPath substepsFolderFor(final IProject project) {
        return folderFor(SubstepsPreferences.SUBSTEPS_FOLDER, project);
    }


    private IPath folderFor(final SubstepsPreferences preference, final IProject project) {
        final String folder = preferenceLookupFactory.preferencesFor(project).valueFor(preference.key());
        if (folder != null && !folder.isEmpty()) {
            return project.getFolder(folder).getLocation();
        }
        return project.getLocation();
    }
}
