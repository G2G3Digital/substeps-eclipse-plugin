package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.technophobia.substeps.FeatureRunnerPlugin;

public class ProjectToJavaProjectTransformer implements
		Transformer<IProject, IJavaProject> {

	@Override
	public IJavaProject to(final IProject project) {
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				return JavaCore.create(project);
			} else {
				warn("Could not transform project " + project.getName()
						+ " to a java project");
			}
		} catch (final CoreException e) {
			warn("Could not transform project " + project.getName()
					+ " to java project: " + e.getMessage());
		}
		return null;
	}

	private void warn(final String message) {
		FeatureRunnerPlugin.log(Status.WARNING, message);
	}

}
