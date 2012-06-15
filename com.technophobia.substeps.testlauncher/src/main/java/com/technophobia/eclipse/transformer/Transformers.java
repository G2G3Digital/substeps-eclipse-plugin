package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;

public class Transformers {

    public static IJavaProject projectToJavaProject(final IProject project) {
        return new ProjectToJavaProjectTransformer().to(project);
    }


    public static IFile selectionToFileOrNull(final ISelection selection) {
        return new SelectionToFileOrNullTransformer().to(selection);
    }
}
