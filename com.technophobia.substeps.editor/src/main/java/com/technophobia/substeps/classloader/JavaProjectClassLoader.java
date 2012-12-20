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
package com.technophobia.substeps.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.JavaRuntime;

import com.technophobia.substeps.FeatureEditorPlugin;

/**
 * Classloader that loads entries within a java project into a delegate
 * classloader
 * 
 * @author sforbes
 * 
 */
public class JavaProjectClassLoader extends ClassLoader {

    private final ClassLoader classLoader;


    public JavaProjectClassLoader(final IJavaProject project) {
        super();
        this.classLoader = createProjectClassLoader(project);
    }


    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        return classLoader.loadClass(name);
    }


    /**
     * Looks up classpath entries for project, and creates a new Classloader
     * with them
     * 
     * @param project
     *            The java project
     * @return Classloader with all classpath entries in project
     */
    private ClassLoader createProjectClassLoader(final IJavaProject project) {
        final String[] classPathEntries = classPathEntriesFor(project);
        final List<URL> urls = new ArrayList<URL>(classPathEntries.length);
        for (final String entry : classPathEntries) {
            final URL url = entryToUrl(entry);
            if (url != null) {
                urls.add(url);
            }
        }
        return createUrlClassLoaderFor(urls, project);
    }


    /**
     * Finds all classpath entries in project
     * 
     * @param project
     *            The current project
     * @return All classpath entries
     */
    private String[] classPathEntriesFor(final IJavaProject project) {
        try {
            return JavaRuntime.computeDefaultRuntimeClassPath(project);
        } catch (final CoreException e) {
            FeatureEditorPlugin.instance().error(
                    "Could not get classpath entries for project " + project.getProject().getName()
                            + ", returning empty array");
            return new String[0];
        }
    }


    /**
     * Converts a classpath entry to a url
     * 
     * @param The
     *            classpath entry
     * @return url representation of the entry
     */
    private URL entryToUrl(final String entry) {
        try {
            final IPath path = new Path(entry);
            final URL url = path.toFile().toURI().toURL();
            return url;
        } catch (final MalformedURLException ex) {
            FeatureEditorPlugin.instance().error("classpath entry " + entry + " could not be mapped to a url");
            return null;
        }
    }


    /**
     * Creates a new UrlClassLoader with the specified urls and java project
     * 
     * @param urls
     *            To be loaded in the classloader
     * @param project
     *            which contains the classes
     * @return Classloader for this java project
     */
    private ClassLoader createUrlClassLoaderFor(final List<URL> urls, final IJavaProject project) {
        return new URLClassLoader(urls.toArray(new URL[urls.size()]), project.getClass().getClassLoader());
    }
}
