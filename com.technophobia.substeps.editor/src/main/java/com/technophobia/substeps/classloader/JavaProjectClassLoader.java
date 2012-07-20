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


    private String[] classPathEntriesFor(final IJavaProject project) {
        try {
            return JavaRuntime.computeDefaultRuntimeClassPath(project);
        } catch (final CoreException e) {
            FeatureEditorPlugin.log(IStatus.ERROR, "Could not get classpath entries for project "
                    + project.getProject().getName() + ", returning empty array");
            return new String[0];
        }
    }


    private URL entryToUrl(final String entry) {
        try {
            final IPath path = new Path(entry);
            final URL url = path.toFile().toURI().toURL();
            return url;
        } catch (final MalformedURLException ex) {
            FeatureEditorPlugin.log(IStatus.ERROR, "classpath entry " + entry + " could not be mapped to a url");
            return null;
        }
    }


    private ClassLoader createUrlClassLoaderFor(final List<URL> urls, final IJavaProject project) {
        return new URLClassLoader(urls.toArray(new URL[urls.size()]), project.getClass().getClassLoader());
    }
}
