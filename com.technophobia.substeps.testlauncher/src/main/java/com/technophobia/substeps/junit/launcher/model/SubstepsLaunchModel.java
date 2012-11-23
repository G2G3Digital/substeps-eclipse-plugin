/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.junit.launcher.model;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;
import com.technophobia.substeps.junit.launcher.SubstepsFeatureLaunchShortcut;
import com.technophobia.substeps.junit.launcher.config.SubstepsLaunchConfigWorkingCopyDecorator;

public class SubstepsLaunchModel implements LaunchModel {

    private String projectName;
    private String featureFile;
    private String substepsFile;


    public SubstepsLaunchModel() {
        // Default constructor
    }


    public SubstepsLaunchModel(final String projectName, final String featureFile, final String substepsFile) {
        this.projectName = projectName;
        this.featureFile = featureFile;
        this.substepsFile = substepsFile;
    }


    @Override
    public void saveTo(final ILaunchConfigurationWorkingCopy config) {
        config.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_FEATURE_PROJECT, projectName);
        config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);

        config.setAttribute(SubstepsFeatureLaunchShortcut.ATTR_FEATURE_FILE, featureFile);

        // config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
        // vmArgs(featureFile, project()));

        // final Collection<String> stepImplementationClasses =
        // FeatureEditorPlugin.instance()
        // .getStepImplementationProvider().stepImplementationClasses(project());
        // config.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_STEP_IMPLEMENTATION_CLASSES,
        // createStringFrom(stepImplementationClasses));

        config.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE, substepsFile);

        config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                SubstepsLaunchConfigWorkingCopyDecorator.FEATURE_TEST);
        config.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_KEEPRUNNING, false);
        config.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_TEST_CONTAINER, "");
        config.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_TEST_RUNNER_KIND,
                SubstepsLaunchConfigurationConstants.JUNIT4_TEST_KIND_ID);
    }


    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }


    public String getFeatureFile() {
        return featureFile;
    }


    public void setFeatureFile(final String featureFile) {
        this.featureFile = featureFile;
    }


    public String getSubstepsFile() {
        return substepsFile;
    }


    public void setSubstepsFile(final String substepsFile) {
        this.substepsFile = substepsFile;
    }
}
