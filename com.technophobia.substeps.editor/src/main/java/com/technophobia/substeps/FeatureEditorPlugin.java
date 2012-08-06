/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.technophobia.eclipse.transformer.ResourceToProjectTransformer;
import com.technophobia.substeps.step.MappedStepImplementationsManager;
import com.technophobia.substeps.step.ProjectStepImplementationLoader;
import com.technophobia.substeps.step.StepImplementationManager;

/**
 * BundleActivator/general bundle aware class for managing things such as
 * logging, requesting the bundle context etc
 * 
 * @author sforbes
 * 
 */
public class FeatureEditorPlugin implements BundleActivator {

    private static final String PLUGIN_ID = "com.technophobia.substeps.editor";

    private static FeatureEditorPlugin pluginInstance;

    private BundleContext context;
    private ResourceBundle resourceBundle;
    private ILog log;

    private final MappedStepImplementationsManager<IProject> stepImplementationManager;


    public FeatureEditorPlugin() {
        super();
        FeatureEditorPlugin.pluginInstance = this;
        this.stepImplementationManager = new MappedStepImplementationsManager<IProject>(
                new ResourceToProjectTransformer(), new ProjectStepImplementationLoader());
    }


    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        context = bundleContext;
        log = Platform.getLog(bundleContext.getBundle());
        try {
            resourceBundle = ResourceBundle.getBundle("com.technophobia.substeps.FeatureEditorResources");
        } catch (final MissingResourceException x) {
            resourceBundle = null;
        }

        addStepImplementationsFromDependencies();
    }


    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        resourceBundle = null;
        log = null;
    }


    public BundleContext getBundleContext() {
        return context;
    }


    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }


    public StepImplementationManager getStepImplementationManager() {
        return stepImplementationManager;
    }


    public static void log(final int status, final String message) {
        instance().log.log(new Status(status, PLUGIN_ID, message));
    }


    public static FeatureEditorPlugin instance() {
        return pluginInstance;
    }


    private void addStepImplementationsFromDependencies() {
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProject[] projects = workspace.getRoot().getProjects();
        for (final IProject project : projects) {
            stepImplementationManager.load(project);
        }
    }
}
