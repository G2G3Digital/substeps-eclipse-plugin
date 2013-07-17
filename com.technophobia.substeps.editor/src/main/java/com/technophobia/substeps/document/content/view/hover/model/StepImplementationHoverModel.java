package com.technophobia.substeps.document.content.view.hover.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.model.StepImplementation;

public class StepImplementationHoverModel extends HoverModel {

    public StepImplementationHoverModel(final String header, final String body, final String location) {
        super(header, body, location);
    }


    public StepImplementationHoverModel(final StepImplementation stepImplementation, final IProject currentProject,
            final ProjectJavaDocLocator<StepImplementation> javadocLocator) {
        super(headerFor(stepImplementation), bodyFor(stepImplementation, currentProject, javadocLocator),
                locationOf(stepImplementation));
    }


    private static String locationOf(final StepImplementation stepImplementation) {
        return stepImplementation.getImplementedIn().getName() + "." + stepImplementation.getMethod().getName();
    }


    private static String headerFor(final StepImplementation stepImplementation) {
        return "Step: " + stepImplementation.getKeyword();
    }


    private static String bodyFor(final StepImplementation stepImplementation, final IProject currentProject,
            final ProjectJavaDocLocator<StepImplementation> javadocLocator) {
        final IJavaProject project = JavaCore.create(currentProject);

        return javadocLocator.formattedJavaDocFor(stepImplementation, project);
    }
}
