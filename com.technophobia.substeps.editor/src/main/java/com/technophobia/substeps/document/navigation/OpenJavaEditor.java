package com.technophobia.substeps.document.navigation;

import static com.technophobia.substeps.FeatureEditorPlugin.instance;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;

/**
 * Static utility methods to help opening a Java file - source or compiled -
 * given a variety of Java class meta data.
 * 
 * @author rpopple
 * 
 */
public class OpenJavaEditor {

    public static IEditorPart open(final IJavaProject project, final Method method) {
        final IMethod methodType = findMethod(project, method);
        return methodType != null ? open(project, methodType) : null;
    }


    private static IEditorPart open(final IJavaProject project, final IJavaElement javaElement) {
        try {
            return JavaUI.openInEditor(javaElement);
        } catch (final CoreException e) {
            final String error = "Unable to open editor for IProject:" + project + " and IJavaElement:" + javaElement;
            instance().error(error, e);
            throw new RuntimeException(e);
        }
    }


    private static IType findClass(final IJavaProject project, final Class<?> clazz) {
        try {
            return project.findType(clazz.getName());
        } catch (final JavaModelException e) {
            final String error = "Unable to find IType for IProject:" + project + " and Class:" + clazz;
            instance().error(error, e);
            throw new RuntimeException(e);
        }
    }


    private static IMethod findMethod(final IJavaProject project, final Method method) {
        final IType classType = findClass(project, method.getDeclaringClass());
        if (classType != null) {
            return classType.getMethod(method.getName(), parameterTypes(method, classType.isBinary()));
        }
        return null;
    }


    private static String[] parameterTypes(final Method method, final boolean isResolved) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String[] types = new String[parameterTypes.length];
        for (int index = 0; index < parameterTypes.length; index++) {
            types[index] = typeSignature(parameterTypes[index], isResolved);
        }
        return types;
    }


    private static final String typeSignature(final Class<?> clazz, final boolean isResolved) {
        // The signature depends whether we are looking at a compiled class or
        // java source.
        return Signature.createTypeSignature(clazz.getName(), isResolved);
    }

}
