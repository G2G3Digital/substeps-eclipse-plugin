package com.technophobia.substeps.syntax;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.eclipse.transformer.ProjectToJavaProjectTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.classloader.ClassLoadedClassAnalyser;
import com.technophobia.substeps.classloader.JavaProjectClassLoader;
import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.PatternMap;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.runner.runtime.ClassLocator;
import com.technophobia.substeps.runner.runtime.StepClassLocator;
import com.technophobia.substeps.runner.syntax.DefaultSyntaxErrorReporter;
import com.technophobia.substeps.runner.syntax.SyntaxBuilder;
import com.technophobia.substeps.runner.syntax.SyntaxErrorReporter;
import com.technophobia.substeps.supplier.Transformer;

public class ProjectToSyntaxTransformer implements Transformer<IProject, Syntax> {

    ProjectToSyntaxTransformer() {
        // package scope constructor to encourage use of the
        // CachingProjectToSyntaxTransformer
    }


    @Override
    public Syntax from(final IProject project) {

        final IJavaProject javaProject = new ProjectToJavaProjectTransformer().from(project);
        final ClassLoader classLoader = new JavaProjectClassLoader(javaProject);
        final Set<String> outputFolders = outputFoldersForProject(javaProject);
        final File projectFile = new File(projectLocationPath(javaProject).toOSString());

        final List<Class<?>> stepClasses = new ArrayList<Class<?>>();
        for (final String outputFolder : outputFolders) {
            final ClassLocator classLocator = new StepClassLocator(outputFolder, classLoader);
            stepClasses.addAll(stepClasses(outputFolder, classLocator));
            // augment step classes with externally dependent classes

        }
        stepClasses.addAll(externalDependenciesFor(project, classLoader));

        try {
            return buildSyntaxFor(projectFile, stepClasses, classLoader, syntaxErrorReporterFor(project));
        } catch (final RuntimeException ex) {
            FeatureEditorPlugin.instance().warn(
                    "Error when building syntax for project " + project + ": " + ex.getMessage(), ex);
            final Syntax nullSyntax = new Syntax();
            nullSyntax.setSubStepsMap(new PatternMap<ParentStep>());
            return nullSyntax;
        }
    }


    private Collection<Class<?>> externalDependenciesFor(final IProject project, final ClassLoader classLoader) {
        final List<String> externalDependencies = FeatureEditorPlugin.instance().externalDependencyStepClasses(project);
        final Collection<Class<?>> classes = new ArrayList<Class<?>>(externalDependencies.size());
        for (final String className : externalDependencies) {
            final Class<?> clazz = loadClass(className, classLoader);
            if (clazz != null) {
                classes.add(clazz);
            }
        }
        return Collections.unmodifiableCollection(classes);
    }


    private Class<?> loadClass(final String stepClassName, final ClassLoader classLoader) {
        try {
            return classLoader.loadClass(stepClassName);
        } catch (final ClassNotFoundException ex) {
            FeatureEditorPlugin.instance().error("Could not load class " + stepClassName, ex);
            return null;
        }
    }


    @SuppressWarnings("unused")
    protected SyntaxErrorReporter syntaxErrorReporterFor(final IProject project) {
        return new DefaultSyntaxErrorReporter();
    }


    protected Syntax buildSyntaxFor(final File projectFile, final List<Class<?>> stepClasses,
            final ClassLoader classLoader, final SyntaxErrorReporter syntaxErrorReporter) {
        return SyntaxBuilder.buildSyntax(stepClasses, projectFile, true, null,
                new ClassLoadedClassAnalyser(classLoader), true, syntaxErrorReporter);
    }


    private Set<String> outputFoldersForProject(final IJavaProject project) {
        final Set<String> outputFolders = new HashSet<String>();
        final IPath projectLocation = projectLocationPath(project);

        try {
            final IPath defaultOutputLocation = project.getOutputLocation();
            if (defaultOutputLocation != null) {
                final IPath fullPath = appendPathTo(projectLocation, defaultOutputLocation);
                if (fullPath.toFile().exists()) {
                    outputFolders.add(fullPath.toOSString());
                }
            }
            for (final IClasspathEntry entry : project.getRawClasspath()) {
                if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    final IPath outputLocation = entry.getOutputLocation();
                    if (outputLocation != null) {
                        final IPath fullPath = appendPathTo(projectLocation, outputLocation);
                        if (fullPath.toFile().exists()) {
                            outputFolders.add(fullPath.toOSString());
                        }
                    }
                }
            }
        } catch (final JavaModelException ex) {
            FeatureEditorPlugin.instance().warn(
                    "Could not get output folder location for project " + project.getElementName());
        }

        return outputFolders;
    }


    private IPath appendPathTo(final IPath projectLocation, final IPath outputLocation) {
        return projectLocation.append(outputLocation.removeFirstSegments(1));
    }


    private IPath projectLocationPath(final IJavaProject project) {
        return project.getResource().getLocation().makeAbsolute();
    }


    private List<Class<?>> stepClasses(final String outputFolder, final ClassLocator classLocator) {
        return toList(classLocator.fromPath(outputFolder));
    }


    private List<Class<?>> toList(final Iterator<Class<?>> it) {
        final List<Class<?>> list = new ArrayList<Class<?>>();
        while (it.hasNext()) {
            final Class<?> nextClass = it.next();
            if (nextClass != null) {
                list.add(nextClass);
            }
        }
        return list;
    }

}
