package com.technophobia.substeps.nature;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPersistentPreferenceStore;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.preferences.SubstepsPreferences;
import com.technophobia.substeps.supplier.Transformer;

public class SubstepsCompatibilityChecker {

    private static final String[] DEFAULT_FEATURE_FOLDER_LOCATIONS = { "features", "src/main/resources/features",
            "src/test/resources/features" };

    private static final String[] DEFAULT_SUBSTEPS_FOLDER_LOCATIONS = { "substeps", "src/main/resources/substeps",
            "src/test/resources/substeps" };

    private final Transformer<IProject, IPersistentPreferenceStore> projectToPreferenceLookup;


    public SubstepsCompatibilityChecker(
            final Transformer<IProject, IPersistentPreferenceStore> projectToPreferenceLookup) {
        this.projectToPreferenceLookup = projectToPreferenceLookup;
    }


    public boolean isCandidateForAddingNature(final IProject project) {

        final boolean isSubstepsProject = SubstepsNature.isSubstepsProject(project);
        final boolean hasBeenProcessed = hasProcessedPreferenceBeenSaved(project);
        final boolean hasSubstepsFolders = hasFolder(DEFAULT_FEATURE_FOLDER_LOCATIONS, project)
                || hasFolder(DEFAULT_SUBSTEPS_FOLDER_LOCATIONS, project);

        return !isSubstepsProject && !hasBeenProcessed && hasSubstepsFolders;
    }


    public void markProjectAsCompatibilityChecked(final IProject project) {
        try {
            final IPersistentPreferenceStore preferenceStore = projectToPreferenceLookup.from(project);
            preferenceStore.setValue(SubstepsPreferences.SUBSTEPS_COMPATIBILITY_CHECKED.key(), true);
            preferenceStore.save();
        } catch (final IOException ex) {
            FeatureEditorPlugin.instance().error(
                    "Could not save the property " + SubstepsPreferences.SUBSTEPS_COMPATIBILITY_CHECKED.key()
                            + " to the preference store", ex);
        }
    }


    private boolean hasProcessedPreferenceBeenSaved(final IProject project) {
        final IPersistentPreferenceStore preferenceStore = projectToPreferenceLookup.from(project);
        return preferenceStore.getBoolean(SubstepsPreferences.SUBSTEPS_COMPATIBILITY_CHECKED.key());
    }


    private boolean hasFolder(final String[] folders, final IProject project) {
        for (final String folder : folders) {
            if (project.getFolder(folder).exists()) {
                return true;
            }
        }
        return false;
    }
}
