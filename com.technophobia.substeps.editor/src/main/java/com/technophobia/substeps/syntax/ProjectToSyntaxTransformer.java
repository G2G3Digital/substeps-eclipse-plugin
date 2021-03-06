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

import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.eclipse.transformer.ProjectToJavaProjectTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.classloader.ClassLoadedClassAnalyser;
import com.technophobia.substeps.classloader.JavaProjectClassLoader;
import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.PatternMap;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.nature.SubstepsNature;
import com.technophobia.substeps.runner.runtime.ClassLocator;
import com.technophobia.substeps.runner.runtime.StepClassLocator;
import com.technophobia.substeps.runner.syntax.DefaultSyntaxErrorReporter;
import com.technophobia.substeps.runner.syntax.SyntaxBuilder;
import com.technophobia.substeps.runner.syntax.SyntaxErrorReporter;
import com.technophobia.substeps.supplier.Transformer;

public class ProjectToSyntaxTransformer implements Transformer<IProject, Syntax> {

    private final ProjectManager projectManager;


    ProjectToSyntaxTransformer(final ProjectManager projectManager) {
        this.projectManager = projectManager;
        // package scope constructor to encourage use of the
        // CachingProjectToSyntaxTransformer
    }


    @Override
    public Syntax from(final IProject project) {

        if (SubstepsNature.isSubstepsProject(project)) {
            final IJavaProject javaProject = new ProjectToJavaProjectTransformer().from(project);
            if (javaProject != null) {
                final ClassLoader classLoader = new JavaProjectClassLoader(javaProject);
                final Set<String> outputFolders = outputFoldersForProject(javaProject);
                final File substepsFolder = new File(projectManager.substepsFolderFor(project).toOSString());

                final List<Class<?>> stepClasses = new ArrayList<Class<?>>();
                for (final String outputFolder : outputFolders) {
                    final ClassLocator classLocator = new StepClassLocator(outputFolder, classLoader);
                    stepClasses.addAll(stepClasses(outputFolder, classLocator));

                }
                // augment step classes with externally dependent classes
                stepClasses.addAll(externalDependenciesFor(project, classLoader));

                try {
                    return buildSyntaxFor(project, substepsFolder, stepClasses, classLoader,
                            syntaxErrorReporterFor(project));
                } catch (final RuntimeException ex) {
                    FeatureEditorPlugin.instance().warn(
                            "Error when building syntax for project " + project + ": " + ex.getMessage(), ex);
                }
            }
        }
        // If we get to here, we can't resolve a valid syntax, return a null one
        final Syntax nullSyntax = new Syntax();
        nullSyntax.setSubStepsMap(new PatternMap<ParentStep>());
        return nullSyntax;
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


    protected SyntaxErrorReporter syntaxErrorReporterFor(final IProject project) {
        return new DefaultSyntaxErrorReporter();
    }


    protected Syntax buildSyntaxFor(final IProject project, final File substepsFolder,
            final List<Class<?>> stepClasses, final ClassLoader classLoader,
            final SyntaxErrorReporter syntaxErrorReporter) {
        return SyntaxBuilder.buildSyntax(stepClasses, substepsFolder, true, null, new ClassLoadedClassAnalyser(
                classLoader), false, syntaxErrorReporter);
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
