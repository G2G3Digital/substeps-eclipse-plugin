package com.technophobia.substeps.syntax;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import com.technophobia.eclipse.log.PluginLogger;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.observer.CacheMonitor;
import com.technophobia.substeps.supplier.CachingResultTransformer;
import com.technophobia.substeps.supplier.Transformer;

public class CachingProjectToSyntaxTransformer implements CachingResultTransformer<IProject, Syntax>,
        CacheMonitor<IProject> {

    private final Transformer<IProject, Syntax> delegate;
    private final Map<IProject, Syntax> cache;
    private final PluginLogger pluginLogger;


    public CachingProjectToSyntaxTransformer() {
        // Default constructor using 'real' project to syntax transformer
        this(new ProjectToSyntaxTransformer(), FeatureEditorPlugin.instance());
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
        if (!cache.containsKey(project)) {
            pluginLogger.info("No syntax cached for project " + project.getName() + ", generating now");
            cache.put(project, delegate.from(project));
        }
        return cache.get(project);
    }


    @Override
    public void refreshCacheFor(final IProject project) {
        pluginLogger.info("Clearing cache for project " + project);
        if (cache.containsKey(project)) {
            cache.remove(project);
        } else {
            pluginLogger.warn("Trying to refresh cache for project " + project.getName()
                    + ", but it does not yet exist");
        }
    }
}
