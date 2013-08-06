package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.message.SubstepsEditorMessages;
import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.supplier.Transformer;

public class JavaDocForStepImplementationLocator implements ProjectJavaDocLocator<StepImplementation> {

    private final ProjectJavaDocLocator<StepImplTypeContext> sourceJavaDocLocator;
    private final ProjectJavaDocLocator<StepImplTypeContext> binaryJavaDocLocator;


    public JavaDocForStepImplementationLocator(
            final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer) {
        this(new JavaDocForSourceStepLocator(stepDescriptorToStringTransformer), new JavaDocForBinaryStepLocator(
                stepDescriptorToStringTransformer));
    }


    public JavaDocForStepImplementationLocator(final ProjectJavaDocLocator<StepImplTypeContext> sourceJavaDocLocator,
            final ProjectJavaDocLocator<StepImplTypeContext> binaryJavaDocLocator) {
        this.sourceJavaDocLocator = sourceJavaDocLocator;
        this.binaryJavaDocLocator = binaryJavaDocLocator;
    }


    @Override
    public String formattedJavaDocFor(final StepImplementation stepImplementation, final IJavaProject project) {
        final IType type = locateTypeForStep(stepImplementation, project);

        if (type != null) {
            final String javadoc = type.isBinary() ? binaryTypeJavadoc(stepImplementation, project, type)
                    : sourceTypeJavadoc(stepImplementation, project, type);

            return isNotBlank(javadoc) ? javadoc : noJavaDocMessage();
        }
        return null;
    }


    private String noJavaDocMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append(SubstepsEditorMessages.StepImplementationHoverModel_No_JavaDoc);
        sb.append("\n\n");
        sb.append(SubstepsEditorMessages.StepImplementationHoverModel_No_JavaDoc_Recommendation);
        return sb.toString();
    }


    private boolean isNotBlank(final String content) {
        return content != null && content.trim().length() > 0;
    }


    private IType locateTypeForStep(final StepImplementation stepImplementation, final IJavaProject project) {
        try {
            return project.findType(stepImplementation.getImplementedIn().getName());
        } catch (final JavaModelException ex) {
            FeatureEditorPlugin.instance().error(
                    "Could not find type for step implementation " + stepImplementation + " in project " + project, ex);
            return null;
        }
    }


    private String binaryTypeJavadoc(final StepImplementation stepImplementation, final IJavaProject project,
            final IType type) {
        return binaryJavaDocLocator.formattedJavaDocFor(StepImplTypeContext.with(type, stepImplementation), project);
    }


    private String sourceTypeJavadoc(final StepImplementation stepImplementation, final IJavaProject project,
            final IType type) {
        return sourceJavaDocLocator.formattedJavaDocFor(StepImplTypeContext.with(type, stepImplementation), project);
    }
}
