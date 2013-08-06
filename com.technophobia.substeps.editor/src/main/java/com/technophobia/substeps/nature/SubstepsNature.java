package com.technophobia.substeps.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.technophobia.eclipse.project.nature.ProjectNatureUpdater;
import com.technophobia.substeps.FeatureEditorPlugin;

public class SubstepsNature implements IProjectNature {

    public static final String NATURE_ID = "com.technophobia.substeps.editor.substepsNature";

    private IProject project;

    private static final ProjectNatureUpdater PROJECT_NATURE_UPDATER = new ProjectNatureUpdater(NATURE_ID);


    public static boolean isSubstepsProject(final IProject project) {
        try {
            return project.hasNature(NATURE_ID);
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error(
                    "Could not determine whether project " + project.getName() + " is a substeps project", ex);
            return false;
        }
    }


    public static void ensureProjectHasNature(final IProject project) {
        PROJECT_NATURE_UPDATER.ensureProjectHasNature(project);
    }


    public static void ensureProjectDoesNotHaveNature(final IProject project) {
        PROJECT_NATURE_UPDATER.ensureProjectDoesNotHaveNature(project);
    }


    @Override
    public void configure() throws CoreException {
        // No-op
    }


    @Override
    public void deconfigure() throws CoreException {
        // No-op
    }


    @Override
    public IProject getProject() {
        return project;
    }


    @Override
    public void setProject(final IProject project) {
        this.project = project;
    }
}
