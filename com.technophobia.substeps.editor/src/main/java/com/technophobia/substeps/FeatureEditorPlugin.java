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
package com.technophobia.substeps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.technophobia.eclipse.log.PluginLogger;
import com.technophobia.eclipse.preference.PreferenceLookup;
import com.technophobia.eclipse.preference.PreferenceLookupFactory;
import com.technophobia.eclipse.project.ProjectEventType;
import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.eclipse.project.ProjectObserver;
import com.technophobia.eclipse.project.PropertyBasedProjectManager;
import com.technophobia.eclipse.project.cache.CacheAwareProjectManager;
import com.technophobia.eclipse.transformer.ResourceToProjectTransformer;
import com.technophobia.substeps.event.SubstepsFolderChangedListener;
import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.preferences.SubstepsProjectPreferenceLookupFactory;
import com.technophobia.substeps.render.ParameterisedStepImplementationRenderer;
import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.ProjectStepDescriptorProvider;
import com.technophobia.substeps.step.ProjectStepImplementationLoader;
import com.technophobia.substeps.step.ProjectStepImplementationProvider;
import com.technophobia.substeps.step.ProjectSuggestionProvider;
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

    public static final String PLUGIN_ID = "com.technophobia.substeps.editor";

    private static final String SUBSTEPS_FOLDER_CHANGED_EXTENSION_POINT_ID = "com.technophobia.substeps.editor.folderMonitorSubsteps";

    private static FeatureEditorPlugin pluginInstance;

    private BundleContext context;
    private ResourceBundle resourceBundle;
    private ILog log;

    private ProvidedSuggestionManager suggestionManager;
    private CachingResultTransformer<IProject, Syntax> projectToSyntaxTransformer;

    private ProjectObserver projectObserver;
    private final ProjectManager projectManager;

    private final PreferenceLookupFactory<IProject> preferenceLookupFactory;


    @SuppressWarnings("unchecked")
    public FeatureEditorPlugin() {
        super();
        FeatureEditorPlugin.pluginInstance = this;
        this.preferenceLookupFactory = new SubstepsProjectPreferenceLookupFactory(PLUGIN_ID,
                (IPersistentPreferenceStore) getPreferenceStore());
        this.projectManager = new PropertyBasedProjectManager(preferenceLookupFactory);
        this.projectToSyntaxTransformer = new CachingProjectToSyntaxTransformer(projectManager, preferenceLookupFactory);
        this.projectObserver = new CacheAwareProjectManager(projectToSyntaxTransformer);
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

        projectObserver.registerFrameworkListeners();

        addSuggestionProviders();
    }


    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        projectObserver.unregisterFrameworkListeners();
        projectObserver = null;
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


    public ProjectManager projectManager() {
        return projectManager;
    }


    public ContextualSuggestionManager getSuggestionManager() {
        if (suggestionManager == null) {
            suggestionManager = new ProvidedSuggestionManager(new ResourceToProjectTransformer());

            // New suggestion providers means syntax should now have changed, re
            // update it
            refreshAllProjectSyntaxes();
        }
        return suggestionManager;
    }


    public ProjectObserver getProjectObserver() {
        return projectObserver;
    }


    public Syntax syntaxFor(final IProject project) {
        return projectToSyntaxTransformer.from(project);
    }


    public ProjectStepImplementationProvider getStepImplementationProvider() {
        return (ProjectStepImplementationProvider) getSuggestionManager();
    }


    public PreferenceLookup preferenceLookupFor(final IProject project) {
        return preferenceLookupFactory.preferencesFor(project);
    }


    public List<String> externalDependencyStepClasses(final IProject project) {
        final Collection<ProjectSuggestionProvider> providers = ((ProvidedSuggestionManager) getSuggestionManager())
                .providersOfSource(SuggestionSource.EXTERNAL_STEP_IMPLEMENTATION);
        final List<String> stepClasses = new ArrayList<String>();
        if (providers != null) {
            for (final ProjectSuggestionProvider projectSuggestionProvider : providers) {
                if (projectSuggestionProvider instanceof ProjectStepImplementationProvider) {
                    stepClasses.addAll(((ProjectStepImplementationProvider) projectSuggestionProvider)
                            .stepImplementationClasses(project));
                }
            }
        }

        return Collections.unmodifiableList(stepClasses);
    }


    public List<StepDescriptor> externalStepDescriptorsForClassInProject(final String className, final IProject project) {
        final Collection<ProjectSuggestionProvider> providers = ((ProvidedSuggestionManager) getSuggestionManager())
                .providersOfSource(SuggestionSource.EXTERNAL_STEP_IMPLEMENTATION);

        final List<StepDescriptor> stepDescriptors = new ArrayList<StepDescriptor>();
        if (providers != null) {
            for (final ProjectSuggestionProvider projectSuggestionProvider : providers) {
                if (projectSuggestionProvider instanceof ProjectStepDescriptorProvider) {
                    stepDescriptors.addAll(((ProjectStepDescriptorProvider) projectSuggestionProvider)
                            .stepDescriptorsFor(project, className));
                }
            }
        }
        return Collections.unmodifiableList(stepDescriptors);
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


    public Collection<SubstepsFolderChangedListener> substepsFolderChangeListeners() {
        return createSubstepsFolderChangeListeners();
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
        final ProvidedSuggestionManager suggestions = (ProvidedSuggestionManager) getSuggestionManager();
        final ExternalStepImplementationProvider externalSuggestionProvider = new ExternalStepImplementationProvider(
                new ProjectStepImplementationLoader());
        suggestions.addProvider(SuggestionSource.EXTERNAL_STEP_IMPLEMENTATION, externalSuggestionProvider);

        final ProjectSpecificSuggestionProvider projectSpecificSuggestionProvider = new ProjectSpecificSuggestionProvider(
                projectToSyntaxTransformer, new ParameterisedStepImplementationRenderer());
        suggestions.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, projectSpecificSuggestionProvider);

        final SubstepSuggestionProvider substepSuggestionProvider = new SubstepSuggestionProvider(
                projectToSyntaxTransformer);
        suggestions.addProvider(SuggestionSource.SUBSTEP_DEFINITION, substepSuggestionProvider);

        projectObserver.addProjectListener(ProjectEventType.ProjectDependenciesChanged, externalSuggestionProvider);
        projectObserver.addProjectListener(ProjectEventType.ProjectInserted, externalSuggestionProvider);
        projectObserver.addProjectListener(ProjectEventType.ProjectRemoved, externalSuggestionProvider);
        projectObserver.addProjectListener(ProjectEventType.ProjectConfigurationChanged, externalSuggestionProvider);
        projectObserver.addProjectListener(ProjectEventType.ProjectConfigurationChanged,
                projectSpecificSuggestionProvider);
        projectObserver.addProjectListener(ProjectEventType.ProjectConfigurationChanged, substepSuggestionProvider);

        projectObserver.addProjectListener(ProjectEventType.SourceFileAnnotationsChanged,
                projectSpecificSuggestionProvider);
        projectObserver.addSubstepsFileListener(substepSuggestionProvider);
        suggestions.load(ResourcesPlugin.getWorkspace());
    }


    private void refreshAllProjectSyntaxes() {
        final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (final IProject project : projects) {
            projectToSyntaxTransformer.refreshCacheFor(project);
        }
    }


    private Collection<SubstepsFolderChangedListener> createSubstepsFolderChangeListeners() {

        final Collection<SubstepsFolderChangedListener> listeners = new ArrayList<SubstepsFolderChangedListener>();

        final IConfigurationElement[] configurationElements = Platform.getExtensionRegistry()
                .getConfigurationElementsFor(SUBSTEPS_FOLDER_CHANGED_EXTENSION_POINT_ID);
        for (final IConfigurationElement configurationElement : configurationElements) {
            final Object folderChangeListener = folderChangeListenerFor(configurationElement);
            if (folderChangeListener != null && folderChangeListener instanceof SubstepsFolderChangedListener) {
                listeners.add((SubstepsFolderChangedListener) folderChangeListener);
            }
        }
        return Collections.unmodifiableCollection(listeners);
    }


    private Object folderChangeListenerFor(final IConfigurationElement configurationElement) {
        try {
            return configurationElement.createExecutableExtension("class");
        } catch (final CoreException ex) {
            error("Could not create executable extension for configuration element " + configurationElement.getName(),
                    ex);
            return null;
        }
    }
}
