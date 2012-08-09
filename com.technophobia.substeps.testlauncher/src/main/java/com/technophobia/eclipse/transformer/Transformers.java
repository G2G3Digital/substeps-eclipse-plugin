package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

public class Transformers {

    public static IJavaProject projectToJavaProject(final IProject project) {
        return new ProjectToJavaProjectTransformer().from(project);
    }


    public static IFile selectionToFileOrNull(final ISelection selection) {
        return new SelectionToFileOrNullTransformer().from(selection);
    }


    public static IResource editorToResource(final IEditorPart editor) {
        return new EditorToResourceTransformer().from(editor);
    }
}
