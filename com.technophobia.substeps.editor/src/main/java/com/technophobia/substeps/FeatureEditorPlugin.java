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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.technophobia.eclipse.log.Logger;
import com.technophobia.eclipse.transformer.ResourceToProjectTransformer;
import com.technophobia.substeps.document.content.assist.feature.ProjectToSyntaxTransformer;
import com.technophobia.substeps.render.ParameterisedStepImplementationRenderer;
import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.ProjectStepImplementationLoader;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.step.ProvidedSuggestionManager;
import com.technophobia.substeps.step.SuggestionSource;
import com.technophobia.substeps.step.provider.ExternalStepImplementationProvider;
import com.technophobia.substeps.step.provider.ProjectSpecificSuggestionProvider;
import com.technophobia.substeps.step.provider.SubstepSuggestionProvider;

/**
 * BundleActivator/general bundle aware class for managing things such as
 * logging, requesting the bundle context etc
 * 
 * @author sforbes
 * 
 */
public class FeatureEditorPlugin implements BundleActivator, Logger {

    private static final String PLUGIN_ID = "com.technophobia.substeps.editor";

    private static FeatureEditorPlugin pluginInstance;

    private BundleContext context;
    private ResourceBundle resourceBundle;
    private ILog log;

    private final ProvidedSuggestionManager suggestionManager;


    public FeatureEditorPlugin() {
        super();
        FeatureEditorPlugin.pluginInstance = this;
        this.suggestionManager = new ProvidedSuggestionManager(new ResourceToProjectTransformer());
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

        addSuggestionProviders();
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


    public ContextualSuggestionManager getSuggestionManager() {
        return suggestionManager;
    }


    public ProjectStepImplementationProvider getStepImplementationProvider() {
        return suggestionManager;
    }


    @Override
    public void log(final int status, final String message) {
        instance().log.log(new Status(status, PLUGIN_ID, message));
    }


    public static FeatureEditorPlugin instance() {
        return pluginInstance;
    }


    private void addSuggestionProviders() {
        final ProjectToSyntaxTransformer projectToSyntaxTransformer = new ProjectToSyntaxTransformer();

        suggestionManager.addProvider(SuggestionSource.EXTERNAL_STEP_IMPLEMENTATION,
                new ExternalStepImplementationProvider(new ProjectStepImplementationLoader()));

        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION,
                new ProjectSpecificSuggestionProvider(projectToSyntaxTransformer,
                        new ParameterisedStepImplementationRenderer()));

        suggestionManager.addProvider(SuggestionSource.SUBSTEP_DEFINITION, new SubstepSuggestionProvider(
                projectToSyntaxTransformer));

        suggestionManager.load(ResourcesPlugin.getWorkspace());
    }
}
