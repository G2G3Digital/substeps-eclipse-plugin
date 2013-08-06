package com.technophobia.substeps.actions;

import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.nature.SubstepsNature;

public class EnableSubstepsNatureAction extends AbstractSubstepsNatureAction {

    @Override
    protected void updateProject(final IProject project) {
        SubstepsNature.ensureProjectHasNature(project);
    }
}
