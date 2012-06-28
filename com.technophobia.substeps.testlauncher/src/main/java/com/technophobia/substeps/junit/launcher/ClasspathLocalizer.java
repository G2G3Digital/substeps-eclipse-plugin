package com.technophobia.substeps.junit.launcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

import com.technophobia.substeps.FeatureRunnerPlugin;

public class ClasspathLocalizer {

    private final boolean inDevelopmentMode;


    public ClasspathLocalizer(final boolean inDevelopmentMode) {
        this.inDevelopmentMode = inDevelopmentMode;
    }


    public List<String> localizeClasspath(final String... pluginIds) {
        final SubstepsRuntimeClasspathEntry[] entries = entriesForPlugins(pluginIds);
        final List<String> substepsEntries = new ArrayList<String>();

        for (int i = 0; i < entries.length; i++) {
            try {
                addEntry(substepsEntries, entries[i]);
            } catch (final IOException e) {
                Assert.isTrue(false, entries[i].getPluginId() + " is available (required JAR)"); //$NON-NLS-1$
            }
        }
        return substepsEntries;
    }


    private SubstepsRuntimeClasspathEntry[] entriesForPlugins(final String[] pluginIds) {

        final SubstepsRuntimeClasspathEntry[] entries = new SubstepsRuntimeClasspathEntry[pluginIds.length];
        for (int i = 0; i < pluginIds.length; i++) {
            entries[i] = new SubstepsRuntimeClasspathEntry(pluginIds[i], null);
        }

        return entries;
    }


    private void addEntry(final List<String> substepsEntries, final SubstepsRuntimeClasspathEntry entry)
            throws IOException, MalformedURLException {
        final String entryString = entryString(entry);
        if (entryString != null)
            substepsEntries.add(entryString);
    }


    private String entryString(final SubstepsRuntimeClasspathEntry entry) throws IOException, MalformedURLException {
        if (inDevelopmentMode()) {
            try {
                return localURL(entry.developmentModeEntry());
            } catch (final IOException e3) {
                // fall through and try default
            }
        }
        return localURL(entry);
    }


    private boolean inDevelopmentMode() {
        return inDevelopmentMode;
    }


    private String localURL(final SubstepsRuntimeClasspathEntry jar) throws IOException, MalformedURLException {
        final Bundle bundle = FeatureRunnerPlugin.instance().getBundle(jar.getPluginId());
        URL url;
        if (jar.getPluginRelativePath() == null)
            url = bundle.getEntry("/"); //$NON-NLS-1$
        else
            url = bundle.getEntry(jar.getPluginRelativePath());
        if (url == null)
            throw new IOException();
        return FileLocator.toFileURL(url).getFile();
    }
}
