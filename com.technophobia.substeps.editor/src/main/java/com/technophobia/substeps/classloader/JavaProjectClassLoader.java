package com.technophobia.substeps.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
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
            FeatureEditorPlugin.instance().log(
                    IStatus.ERROR,
                    "Could not get classpath entries for project " + project.getProject().getName()
                            + ", returning empty array");
            return new String[0];
        }
    }


    /**
     * Converts a classpath entry to a url
     * 
     * @param entry
     *            The classpath entry
     * @return url representation of the entry
     */
    private URL entryToUrl(final String entry) {
        try {
            final IPath path = new Path(entry);
            final URL url = path.toFile().toURI().toURL();
            return url;
        } catch (final MalformedURLException ex) {
            FeatureEditorPlugin.instance().log(IStatus.ERROR,
                    "classpath entry " + entry + " could not be mapped to a url");
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
