package com.technophobia.substeps.junit.launcher;

import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.supplier.Transformer;

public class DefaultSubstepsLocationFinder implements Transformer<IProject, String> {

    private static final String[] DEFAULT_SUBSTEPS_FOLDER_LOCATIONS = { "substeps", "src/main/resources/substeps",
            "src/test/resources/substeps" };


    @Override
    public String from(final IProject project) {
        for (final String defaultSubstepsFolder : DEFAULT_SUBSTEPS_FOLDER_LOCATIONS) {
            if (project.getFolder(defaultSubstepsFolder).exists()) {
                return defaultSubstepsFolder;
            }
        }

        return null;
    }

}
