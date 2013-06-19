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
package com.technophobia.substeps.syntax;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import com.technophobia.eclipse.log.PluginLogger;
import com.technophobia.eclipse.preference.PreferenceLookupFactory;
import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.nature.SubstepsNature;
import com.technophobia.substeps.observer.CacheMonitor;
import com.technophobia.substeps.supplier.CachingResultTransformer;
import com.technophobia.substeps.supplier.Transformer;

public class CachingProjectToSyntaxTransformer implements CachingResultTransformer<IProject, Syntax>,
        CacheMonitor<IProject> {

    private final Transformer<IProject, Syntax> delegate;
    private final Map<IProject, Syntax> cache;
    private final PluginLogger pluginLogger;


    public CachingProjectToSyntaxTransformer(final ProjectManager projectManager,
            final PreferenceLookupFactory<IProject> projectPreferenceLookupFactory) {
        // Default constructor using 'real' project to syntax transformer
        this(new ProblemValidatingProjectToSyntaxTransformer(projectManager, projectPreferenceLookupFactory),
                FeatureEditorPlugin.instance());
    }


    public CachingProjectToSyntaxTransformer(final Transformer<IProject, Syntax> delegate,
            final PluginLogger pluginLogger) {
        this.delegate = delegate;
        this.pluginLogger = pluginLogger;
        this.cache = new HashMap<IProject, Syntax>();
    }


    @Override
    public Syntax from(final IProject project) {
        pluginLogger.info("Getting syntax for project " + project.getName());
        if (!cache.containsKey(project) && project.isOpen()) {
            pluginLogger.info("No syntax cached for project " + project.getName() + ", generating now");
            cache.put(project, delegate.from(project));
        }
        return cache.get(project);
    }


    @Override
    public void refreshCacheFor(final IProject project) {
        pluginLogger.info("Clearing cache for project " + project);
        if (project.isOpen() && SubstepsNature.isSubstepsProject(project)) {
            cache.put(project, delegate.from(project));
        } else {
            pluginLogger.info("Project " + project + " is closed. Evicting from cache if it was present");
            evictFrom(project);
        }
    }


    @Override
    public void evictFrom(final IProject project) {
        pluginLogger.info("Evicting project " + project + " from cache");
        cache.remove(project);
    }
}
