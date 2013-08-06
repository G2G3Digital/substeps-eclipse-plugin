package com.technophobia.eclipse.javadoc;

import org.eclipse.jdt.core.IJavaProject;

public interface ProjectJavaDocLocator<T> {

    String formattedJavaDocFor(T t, IJavaProject project);
}
