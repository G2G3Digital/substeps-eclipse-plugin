package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Transformer;

public class ProjectToJavaProjectTransformer implements Transformer<IProject, IJavaProject> {

    @Override
    public IJavaProject from(final IProject project) {
        try {
            if (project.hasNature(JavaCore.NATURE_ID)) {
                return JavaCore.create(project);
            }
            FeatureEditorPlugin.instance().log(IStatus.WARNING,
                    "Could not transform project " + project.getName() + " to a java project");
        } catch (final CoreException e) {
            FeatureEditorPlugin.instance().log(IStatus.WARNING,
                    "Could not transform project " + project.getName() + " to java project: " + e.getMessage());
        }
        return null;
    }

}
