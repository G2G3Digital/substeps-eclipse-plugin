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
package com.technophobia.substeps.step;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.glossary.XMLSubstepsGlossarySerializer;
import com.technophobia.substeps.supplier.Transformer;

public class ProjectStepImplementationLoader implements Transformer<IProject, List<StepImplementationsDescriptor>> {

    private final XMLSubstepsGlossarySerializer serializer;


    public ProjectStepImplementationLoader() {
        this.serializer = new XMLSubstepsGlossarySerializer();
    }


    @Override
    public List<StepImplementationsDescriptor> from(final IProject project) {
        final List<StepImplementationsDescriptor> stepImplementationsDescriptors = new ArrayList<StepImplementationsDescriptor>();
        if (isJavaProject(project)) {
            final IJavaProject javaProject = toJavaProject(project);

            final String[] dependencies = findDependenciesFor(javaProject);

            for (final String dependency : dependencies) {
                stepImplementationsDescriptors.addAll(findStepImplementationDescriptorsForDependency(dependency));
            }
        }
        return stepImplementationsDescriptors;
    }


    private boolean isJavaProject(final IProject project) {
        try {
            return project.hasNature(JavaCore.NATURE_ID);
        } catch (final CoreException e) {
            FeatureEditorPlugin.instance().warn(
                    "Could not determine if project " + project.getName() + " is a java project");
            return false;
        }
    }


    private IJavaProject toJavaProject(final IProject project) {
        return JavaCore.create(project);
    }


    private String[] findDependenciesFor(final IJavaProject javaProject) {
        try {
            final IPackageFragmentRoot[] fragmentRoots = javaProject.getPackageFragmentRoots();
            final Collection<String> rootPaths = new ArrayList<String>(fragmentRoots.length);
            for (int i = 0; i < fragmentRoots.length; i++) {
                if (fragmentRoots[i].getKind() == IPackageFragmentRoot.K_BINARY) {
                    final String path = getPathFor(fragmentRoots[i]);
                    if (path != null) {
                        rootPaths.add(path);
                    }
                }
            }
            return rootPaths.toArray(new String[rootPaths.size()]);
        } catch (final JavaModelException ex) {
            FeatureEditorPlugin.instance().warn(
                    "Could not get package fragment roots for project " + javaProject.getProject().getName());
            return new String[0];
        }
    }


    /**
     * Returns the path for this fragment in a OS friendly string. Note that in
     * windows, the method for getting the absolute path varies for source
     * folders and jars
     * 
     * @param fragmentRoot
     *            The fragment containing classes
     * @return The path of this fragment root, os-friendly
     */
    private String getPathFor(final IPackageFragmentRoot fragmentRoot) {
        final IResource resource = fragmentRoot.getResource();
        final IPath path;
        if (resource != null) {
            path = resource.getRawLocation();
        } else {
            path = fragmentRoot.getPath().makeAbsolute();
        }
        return path.toOSString();
    }


    private List<StepImplementationsDescriptor> findStepImplementationDescriptorsForDependency(final String path) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(new File(path));
            final List<StepImplementationsDescriptor> stepImplementationDescriptors = serializer
                    .loadStepImplementationsDescriptorFromJar(jarFile);
            return stepImplementationDescriptors != null ? stepImplementationDescriptors : Collections
                    .<StepImplementationsDescriptor> emptyList();
        } catch (final IOException ex) {
            FeatureEditorPlugin.instance().warn("Could not open jar file " + path);
        } finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            } catch (final IOException e) {
                FeatureEditorPlugin.instance().warn("Could not close jar file " + path);
            }
        }

        return Collections.<StepImplementationsDescriptor> emptyList();
    }
}
