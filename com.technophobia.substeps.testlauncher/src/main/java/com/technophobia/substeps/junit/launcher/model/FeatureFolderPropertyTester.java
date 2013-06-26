package com.technophobia.substeps.junit.launcher.model;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.technophobia.substeps.FeatureEditorPlugin;

public class FeatureFolderPropertyTester extends PropertyTester {

    private static final String PROPERTY_NAME = "isFeatureFolder";


    @Override
    public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
        if (PROPERTY_NAME.equals(property)) {
            final IContainer folder = (IContainer) receiver;
            final IProject project = folder.getProject();
            final IPath featureFolderPath = featureFolderForProject(project);

            return isDescendantOf(folder.getFullPath(), featureFolderPath);
        }
        return false;
    }


    private boolean isDescendantOf(final IPath folder, final IPath featureFolderPath) {
        return featureFolderPath.isPrefixOf(folder);
    }


    private IPath featureFolderForProject(final IProject project) {
        return FeatureEditorPlugin.instance().projectManager().featureFolderFor(project);
    }

}
