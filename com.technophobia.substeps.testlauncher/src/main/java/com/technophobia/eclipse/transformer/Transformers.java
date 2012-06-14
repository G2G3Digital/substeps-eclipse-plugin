package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

public class Transformers {

    public static IJavaProject projectToJavaProject(final IProject project) {
        return new ProjectToJavaProjectTransformer().to(project);
    }
}
