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
