package com.technophobia.substeps.document.content.view.hover.model;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.SubSteps.Step;

public class StepImplementationHoverModel extends HoverModel {

    public StepImplementationHoverModel(final String header, final String body, final String location) {
        super(header, body, location);
    }


    public StepImplementationHoverModel(final StepImplementation stepImplementation) {
        super(headerFor(stepImplementation), bodyFor(stepImplementation), locationOf(stepImplementation));
    }


    private static String headerFor(final StepImplementation stepImplementation) {
        return "Step: " + stepImplementation.getValue();
    }


    private static String bodyFor(final StepImplementation stepImplementation) {
        // try {
        // final IJavaProject project = JavaCore.create((IProject) null);
        // final IType type =
        // project.findType(stepImplementation.getImplementedIn().getName());
        // final IMethod method = findMethod(type, stepImplementation);
        // final String javadoc = method.getAttachedJavadoc(new
        // NullProgressMonitor());
        //
        // if (javadoc != null) {
        // return javadoc;
        // }
        // } catch (final JavaModelException ex) {
        // FeatureEditorPlugin.instance().error("Could not get javadoc for step "
        // + stepImplementation, ex);
        // }
        return "No javadoc";
    }


    private static IMethod findMethod(final IType type, final StepImplementation stepImplementation)
            throws JavaModelException {
        final IMethod[] methods = type.getMethods();
        if (methods != null) {
            for (final IMethod method : methods) {
                final IAnnotation stepAnnotation = method.getAnnotation(Step.class.getName());
                if (stepAnnotation != null) {
                }
            }
        }
        return null;
    }


    private static String locationOf(final StepImplementation stepImplementation) {
        return stepImplementation.getImplementedIn().getName() + "." + stepImplementation.getMethod().getName();
    }
}
