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
package com.technophobia.eclipse.launcher.config;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import com.technophobia.substeps.FeatureRunnerPlugin;

public class SubstepsLaunchConfigurationConstants {

    public static final String ATTR_FEATURE_PROJECT = FeatureRunnerPlugin.PLUGIN_ID + ".FEATURE_PROJECT";
    // public static final String ATTR_FEATURE_FILE =
    // FeatureRunnerPlugin.PLUGIN_ID + ".FEATURE_FILE";
    public static final String ATTR_SUBSTEPS_FILE = FeatureRunnerPlugin.PLUGIN_ID + ".SUBSTEPS_FILE";
    public static final String ATTR_STEP_IMPLEMENTATION_CLASSES = FeatureRunnerPlugin.PLUGIN_ID
            + ".STEP_IMPLEMENTATION_CLASSES";

    public static final String ATTR_FAILURES_NAMES = FeatureRunnerPlugin.PLUGIN_ID + ".FAILURENAMES";
    public static final String ATTR_PORT = FeatureRunnerPlugin.PLUGIN_ID + ".PORT";
    public static final String ATTR_TEST_RUNNER_KIND = FeatureRunnerPlugin.PLUGIN_ID + ".TEST_KIND";
    public static final String ATTR_KEEPRUNNING = FeatureRunnerPlugin.PLUGIN_ID + ".KEEPRUNNING_ATTR";

    public static final String ATTR_TEST_CONTAINER = FeatureRunnerPlugin.PLUGIN_ID + ".CONTAINER";
    public static final String ATTR_TEST_METHOD_NAME = FeatureRunnerPlugin.PLUGIN_ID + ".TESTNAME";

    public static final String JUNIT4_TEST_KIND_ID = "org.eclipse.jdt.junit.loader.junit4";
    public static final String MODE_RUN_QUIETLY_MODE = "runQuietly";

    public static final String ATTR_NO_DISPLAY = FeatureRunnerPlugin.PLUGIN_ID + ".NO_DISPLAY";


    public static IJavaProject getJavaProject(final ILaunchConfiguration config) {
        try {
            final String projectName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    (String) null);
            if (projectName != null && projectName.length() > 0) {
                return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName));
            }
        } catch (final CoreException e) {
            // No-op
        }
        return null;
    }

}
