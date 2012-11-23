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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.launcher.SubstepsFeatureLaunchShortcut;
import com.technophobia.substeps.supplier.Transformer;

public class SubstepsLaunchModelFactory implements LaunchModelFactory {

    private final Transformer<IProject, String> substepsFolderLocator;


    public SubstepsLaunchModelFactory(final Transformer<IProject, String> substepsFolderLocator) {
        this.substepsFolderLocator = substepsFolderLocator;
    }


    @Override
    public LaunchModel createFrom(final IResource resource) {
        final IProject project = resource.getProject();
        final String filePath = resource.getFullPath().removeFirstSegments(1).toOSString();
        final String substepsFolder = substepsFolderLocator.from(project);

        return new SubstepsLaunchModel(project.getName(), filePath, substepsFolder);
    }


    @Override
    public LaunchModel createFrom(final ILaunchConfiguration config) {
        final String projectName = getConfigAttribute(config, SubstepsLaunchConfigurationConstants.ATTR_FEATURE_PROJECT);
        final String featureFile = getConfigAttribute(config, SubstepsFeatureLaunchShortcut.ATTR_FEATURE_FILE);
        final String substepsFile = getConfigAttribute(config, SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE);

        return new SubstepsLaunchModel(projectName, featureFile, substepsFile);
    }


    private String getConfigAttribute(final ILaunchConfiguration config, final String configName) {
        try {
            return config.getAttribute(configName, "");
        } catch (final CoreException e) {
            FeatureRunnerPlugin.log(e);
            return "";
        }
    }
}
