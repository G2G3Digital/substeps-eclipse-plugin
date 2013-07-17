package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import org.eclipse.jdt.core.IJavaProject;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.supplier.Transformer;

public class JavaDocForBinaryStepLocator implements ProjectJavaDocLocator<StepImplTypeContext> {

    private final ProjectJavaDocLocator<StepImplTypeContext>[] subJavaDocBinaryStepLocators;


    @SuppressWarnings("unchecked")
    public JavaDocForBinaryStepLocator(final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer) {
        // When looking at annotations for IMethods in binary types, the
        // annotations use just the full name
        this(new MethodWithAttachedJavadocLocator(new DefaultMethodForStepLocator(Step.class.getName()
                .replace('$', '.'))), new StepImplementationDescriptorJavadocLocator(stepDescriptorToStringTransformer));
    }


    public JavaDocForBinaryStepLocator(final ProjectJavaDocLocator<StepImplTypeContext>... subJavaDocBinaryStepLocators) {
        this.subJavaDocBinaryStepLocators = subJavaDocBinaryStepLocators;
    }


    @Override
    public String formattedJavaDocFor(final StepImplTypeContext stepImplTypeContext, final IJavaProject project) {

        for (final ProjectJavaDocLocator<StepImplTypeContext> subJavaDocBinaryStepLocator : subJavaDocBinaryStepLocators) {
            final String javadoc = subJavaDocBinaryStepLocator.formattedJavaDocFor(stepImplTypeContext, project);
            if (javadoc != null) {
                return javadoc;
            }
        }
        return null;
    }
}
