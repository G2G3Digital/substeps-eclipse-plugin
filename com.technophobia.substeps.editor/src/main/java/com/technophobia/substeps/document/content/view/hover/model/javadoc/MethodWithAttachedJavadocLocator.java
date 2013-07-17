package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.supplier.Transformer;

public class MethodWithAttachedJavadocLocator implements ProjectJavaDocLocator<StepImplTypeContext> {

    private final Transformer<StepImplTypeContext, IMethod> methodLocator;


    public MethodWithAttachedJavadocLocator(final Transformer<StepImplTypeContext, IMethod> methodLocator) {
        this.methodLocator = methodLocator;
    }


    @Override
    public String formattedJavaDocFor(final StepImplTypeContext stepImplTypeContext, final IJavaProject project) {
        final IMethod method = methodLocator.from(stepImplTypeContext);
        if (method != null) {
            try {
                final String attachedJavadoc = method.getAttachedJavadoc(new NullProgressMonitor());
                if (attachedJavadoc != null) {
                    return attachedJavadoc;
                }
            } catch (final StringIndexOutOfBoundsException ex) {
                // Couldn't find javadoc
            } catch (final JavaModelException e) {
                // Couldn't find javadoc
            }
        }

        return null;
    }

}
