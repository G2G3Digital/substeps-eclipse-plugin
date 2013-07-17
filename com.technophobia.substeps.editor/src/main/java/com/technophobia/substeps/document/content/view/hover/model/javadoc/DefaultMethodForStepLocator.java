package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.supplier.Transformer;

public class DefaultMethodForStepLocator implements Transformer<StepImplTypeContext, IMethod> {

    private final String annotationName;


    public DefaultMethodForStepLocator(final String annotationName) {
        this.annotationName = annotationName;
    }


    @Override
    public IMethod from(final StepImplTypeContext ctx) {
        try {
            final IMethod[] methods = ctx.type().getMethods();
            if (methods != null) {
                for (final IMethod method : methods) {
                    // method.getAnnotations()[0].getElementName()
                    final IAnnotation stepAnnotation = findStepAnnotation(method);
                    if (stepAnnotation != null) {
                        if (isAnnotationMatch(ctx.stepImpl(), stepAnnotation)) {
                            return method;
                        }
                    }
                }
            }
        } catch (final JavaModelException ex) {
            FeatureEditorPlugin.instance().error(
                    "Could not find method for step " + ctx.stepImpl() + " in type " + ctx.type().getElementName(), ex);
        }
        return null;
    }


    private IAnnotation findStepAnnotation(final IMethod method) throws JavaModelException {
        for (final IAnnotation annotation : method.getAnnotations()) {
            if (annotationName.equals(annotation.getElementName())) {
                return annotation;
            }
        }
        return null;
    }


    private boolean isAnnotationMatch(final StepImplementation stepImplementation, final IAnnotation stepAnnotation)
            throws JavaModelException {
        final String annotationValue = annotationValue(stepAnnotation);
        if (annotationValue != null) {
            return isStepImplementationMatchForAnnotationValue(stepImplementation, annotationValue);
        }
        return false;
    }


    private String annotationValue(final IAnnotation annotation) {
        try {
            final IMemberValuePair[] valuePairs = annotation.getMemberValuePairs();
            for (final IMemberValuePair valuePair : valuePairs) {
                if ("value".equals(valuePair.getMemberName())) {
                    return (String) valuePair.getValue();
                }
            }
        } catch (final JavaModelException ex) {
            // This can be triggered if the annotation doesn't really exist, so
            // don't log anything, otherwise the logs will be filled with it.
            // Instead, just return null.
        }
        return null;
    }


    private boolean isStepImplementationMatchForAnnotationValue(final StepImplementation stepImplementation,
            final String annotationValue) {
        return stepImplementation.getValue().equals(annotationValue);
    }
}
