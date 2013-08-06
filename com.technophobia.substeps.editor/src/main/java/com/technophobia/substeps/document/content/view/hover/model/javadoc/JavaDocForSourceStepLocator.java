package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.supplier.Transformer;

public class JavaDocForSourceStepLocator implements ProjectJavaDocLocator<StepImplTypeContext> {

    private final Transformer<StepImplTypeContext, IMethod> methodForStepLocator;
    private final Transformer<IMethod, String> javaDocForSourceMethodTransformer;


    public JavaDocForSourceStepLocator(final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer) {
        // When looking at annotations for IMethods in source types, the
        // annotations use just the simple name
        this(new DefaultMethodForStepLocator(Step.class.getSimpleName()), new ASTParsedMethodUnitJavadocCollector(
                stepDescriptorToStringTransformer));
    }


    public JavaDocForSourceStepLocator(final Transformer<StepImplTypeContext, IMethod> methodForStepLocator,
            final Transformer<IMethod, String> javaDocForSourceMethodTransformer) {
        this.methodForStepLocator = methodForStepLocator;
        this.javaDocForSourceMethodTransformer = javaDocForSourceMethodTransformer;
    }


    @Override
    public String formattedJavaDocFor(final StepImplTypeContext stepImplTypeContext, final IJavaProject project) {
        final IMethod method = methodFor(stepImplTypeContext);
        if (method != null) {
            return javaDocForSourceMethodTransformer.from(method);
        }
        return null;
    }


    private IMethod methodFor(final StepImplTypeContext stepImplTypeContext) {
        return methodForStepLocator.from(stepImplTypeContext);
    }
}
