package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.supplier.Transformer;

public class StepImplementationDescriptorJavadocLocator implements ProjectJavaDocLocator<StepImplTypeContext> {

    private final StepDescriptorProvider stepDescriptorProvider;
    private final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer;


    public StepImplementationDescriptorJavadocLocator(
            final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer) {
        this(stepDescriptorToStringTransformer, new StepDescriptorProvider() {
            @Override
            public List<StepDescriptor> descriptorsForClassInProject(final String className, final IProject project) {
                return FeatureEditorPlugin.instance().externalStepDescriptorsForClassInProject(className, project);
            }
        });
    }


    public StepImplementationDescriptorJavadocLocator(
            final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer,
            final StepDescriptorProvider stepDescriptorProvider) {
        this.stepDescriptorToStringTransformer = stepDescriptorToStringTransformer;
        this.stepDescriptorProvider = stepDescriptorProvider;
    }


    @Override
    public String formattedJavaDocFor(final StepImplTypeContext stepImplTypeContext, final IJavaProject project) {

        final StepImplementation stepImplementation = stepImplTypeContext.stepImpl();
        final List<StepDescriptor> stepDescriptors = stepDescriptorProvider.descriptorsForClassInProject(
                stepImplementation.getImplementedIn().getName(), project.getProject());
        for (final StepDescriptor stepDescriptor : stepDescriptors) {
            if (Pattern.matches(stepImplementation.getValue(), stepDescriptor.getExample())) {
                return asString(stepDescriptor);
            }
        }

        return null;
    }


    private String asString(final StepDescriptor stepDescriptor) {
        return stepDescriptorToStringTransformer.from(stepDescriptor);
    }
}
