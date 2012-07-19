package com.technophobia.substeps.junit.launcher.migration;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationMigrationDelegate;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;

public class SubstepsMigrationDelegate implements ILaunchConfigurationMigrationDelegate {

    protected static final String EMPTY_STRING = ""; //$NON-NLS-1$


    public SubstepsMigrationDelegate() {

    }


    @Override
    public boolean isCandidate(final ILaunchConfiguration candidate) throws CoreException {
        final IResource[] mapped = candidate.getMappedResources();
        final IResource target = getResource(candidate);
        if (target == null) {
            return mapped == null;
        } else {
            if (mapped == null) {
                return true;
            } else {
                if (mapped.length != 1) {
                    return true;
                } else {
                    return !target.equals(mapped[0]);
                }
            }
        }
    }


    @Override
    public void migrate(final ILaunchConfiguration candidate) throws CoreException {
        final ILaunchConfigurationWorkingCopy wc = candidate.getWorkingCopy();
        mapResources(wc);
        wc.doSave();
    }


    /**
     * Maps a resource for the given launch configuration.
     * 
     * @param config
     *            working copy
     * @throws CoreException
     *             if an exception occurs mapping resource
     */
    public static void mapResources(final ILaunchConfigurationWorkingCopy config) throws CoreException {
        final IResource resource = getResource(config);
        if (resource == null) {
            config.setMappedResources(null);
        } else {
            config.setMappedResources(new IResource[] { resource });
        }
    }


    /**
     * Returns a resource mapping for the given launch configuration, or
     * <code>null</code> if none.
     * 
     * @param config
     *            working copy
     * @return resource or <code>null</code>
     * @throws CoreException
     *             if an exception occurs mapping resource
     */
    private static IResource getResource(final ILaunchConfiguration config) throws CoreException {
        final String projName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
        final String containerHandle = config.getAttribute(SubstepsLaunchConfigurationConstants.ATTR_TEST_CONTAINER,
                (String) null);
        final String typeName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                (String) null);
        IJavaElement element = null;
        if (containerHandle != null && containerHandle.length() > 0) {
            element = JavaCore.create(containerHandle);
        } else if (projName != null && Path.ROOT.isValidSegment(projName)) {
            final IJavaProject javaProject = getJavaModel().getJavaProject(projName);
            if (javaProject.exists()) {
                if (typeName != null && typeName.length() > 0) {
                    element = javaProject.findType(typeName);
                }
                if (element == null) {
                    element = javaProject;
                }
            } else {
                final IProject project = javaProject.getProject();
                if (project.exists() && !project.isOpen()) {
                    return project;
                }
            }
        }
        IResource resource = null;
        if (element != null) {
            resource = element.getResource();
        }
        return resource;
    }


    /*
     * Convenience method to get access to the java model.
     */
    private static IJavaModel getJavaModel() {
        return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
    }

}
