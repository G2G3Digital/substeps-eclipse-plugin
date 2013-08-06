package com.technophobia.substeps.actions;

import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.nature.SubstepsNature;

public class DisableSubstepsNatureAction extends AbstractSubstepsNatureAction {

    @Override
    protected void updateProject(final IProject project) {
        SubstepsNature.ensureProjectDoesNotHaveNature(project);
    }
}