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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.technophobia.eclipse.log.PluginLogger;
import com.technophobia.eclipse.project.ProjectEventType;
import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.eclipse.project.cache.CacheAwareProjectManager;
import com.technophobia.eclipse.transformer.ResourceToProjectTransformer;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.render.ParameterisedStepImplementationRenderer;
import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.ProjectStepImplementationLoader;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.step.ProvidedSuggestionManager;
import com.technophobia.substeps.step.SuggestionSource;
import com.technophobia.substeps.step.provider.ExternalStepImplementationProvider;
import com.technophobia.substeps.step.provider.ProjectSpecificSuggestionProvider;
import com.technophobia.substeps.step.provider.SubstepSuggestionProvider;
import com.technophobia.substeps.supplier.CachingResultTransformer;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.syntax.CachingProjectToSyntaxTransformer;

/**
 * BundleActivator/general bundle aware class for managing things such as
 * logging, requesting the bundle context etc
 * 
 * @author sforbes
 * 
 */
public class FeatureEditorPlugin extends AbstractUIPlugin implements BundleActivator, PluginLogger {

    private static final String PLUGIN_ID = "com.technophobia.substeps.editor";

    private static FeatureEditorPlugin pluginInstance;

    private BundleContext context;
    private ResourceBundle resourceBundle;
    private ILog log;

    private ProvidedSuggestionManager suggestionManager;
    private CachingResultTransformer<IProject, Syntax> projectToSyntaxTransformer;

    private ProjectManager projectManager;


    @SuppressWarnings("unchecked")
    public FeatureEditorPlugin() {
        super();
        FeatureEditorPlugin.pluginInstance = this;
        this.suggestionManager = new ProvidedSuggestionManager(new ResourceToProjectTransformer());
        this.projectToSyntaxTransformer = new CachingProjectToSyntaxTransformer();
        this.projectManager = new CacheAwareProjectManager(projectToSyntaxTransformer);
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

        projectManager.registerFrameworkListeners();
        addSuggestionProviders();
    }


    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        projectManager.unregisterFrameworkListeners();
        projectManager = null;
        suggestionManager = null;
        projectToSyntaxTransformer = null;
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


    public ProjectManager getProjectManager() {
        return projectManager;
    }


    public Syntax syntaxFor(final IProject project) {
        return projectToSyntaxTransformer.from(project);
    }


    public ProjectStepImplementationProvider getStepImplementationProvider() {
        return suggestionManager;
    }


    public Supplier<IResource> currentResourceSupplier() {
        return new Supplier<IResource>() {
            @Override
            public IResource get() {
                final IEditorPart activeEditor = getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getActiveEditor();
                if (activeEditor != null) {
                    return (IResource) activeEditor.getEditorInput().getAdapter(IResource.class);
                }
                return null;
            }
        };
    }


    @Override
    public void info(final String msg) {
        instance().log.log(new Status(IStatus.INFO, PLUGIN_ID, msg));
    }


    @Override
    public void warn(final String msg) {
        instance().log.log(new Status(IStatus.WARNING, PLUGIN_ID, msg));
    }


    @Override
    public void warn(final String msg, final Throwable ex) {
        instance().log.log(new Status(IStatus.WARNING, PLUGIN_ID, msg, ex));
    }


    @Override
    public void error(final String msg) {
        instance().log.log(new Status(IStatus.ERROR, PLUGIN_ID, msg));
    }


    @Override
    public void error(final String msg, final Throwable t) {
        instance().log.log(new Status(IStatus.ERROR, PLUGIN_ID, msg, t));
    }


    public static FeatureEditorPlugin instance() {
        return pluginInstance;
    }


    private void addSuggestionProviders() {
        final ExternalStepImplementationProvider externalSuggestionProvider = new ExternalStepImplementationProvider(
                new ProjectStepImplementationLoader());
        suggestionManager.addProvider(SuggestionSource.EXTERNAL_STEP_IMPLEMENTATION, externalSuggestionProvider);

        final ProjectSpecificSuggestionProvider projectSpecificSuggestionProvider = new ProjectSpecificSuggestionProvider(
                projectToSyntaxTransformer, new ParameterisedStepImplementationRenderer());
        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, projectSpecificSuggestionProvider);

        final SubstepSuggestionProvider substepSuggestionProvider = new SubstepSuggestionProvider(
                projectToSyntaxTransformer);
        suggestionManager.addProvider(SuggestionSource.SUBSTEP_DEFINITION, substepSuggestionProvider);

        projectManager.addProjectListener(ProjectEventType.ProjectDependenciesChanged, externalSuggestionProvider);
        projectManager.addProjectListener(ProjectEventType.SourceFileAnnotationsChanged,
                projectSpecificSuggestionProvider);
        projectManager.addSubstepsFileListener(substepSuggestionProvider);
        suggestionManager.load(ResourcesPlugin.getWorkspace());
    }

}
