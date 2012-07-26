package com.technophobia.substeps.step;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
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
    public List<StepImplementationsDescriptor> to(final IProject project) {
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
            FeatureEditorPlugin.log(IStatus.WARNING, "Could not determine if project " + project.getName()
                    + " is a java project");
            return false;
        }
    }


    private IJavaProject toJavaProject(final IProject project) {
        return JavaCore.create(project);
    }


    private String[] findDependenciesFor(final IJavaProject javaProject) {
        try {
            final IPackageFragmentRoot[] fragmentRoots = javaProject.getPackageFragmentRoots();
            final String[] rootPaths = new String[fragmentRoots.length];
            for (int i = 0; i < fragmentRoots.length; i++) {
                rootPaths[i] = fragmentRoots[i].getPath().makeAbsolute().toOSString();
            }
            return rootPaths;
        } catch (final JavaModelException ex) {
            FeatureEditorPlugin.log(IStatus.WARNING, "Could not get package fragment roots for project "
                    + javaProject.getProject().getName());
            return new String[0];
        }
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
            FeatureEditorPlugin.log(IStatus.WARNING, "Could not open jar file " + path);
        } finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            } catch (final IOException e) {
                FeatureEditorPlugin.log(IStatus.WARNING, "Could not close jar file " + path);
            }
        }

        return Collections.<StepImplementationsDescriptor> emptyList();
    }
}
