package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.supplier.Transformer;

public class JavaDocVisitor extends ASTVisitor {

    private final StringBuilder description;
    private final StringBuilder example;
    private final StringBuilder expression;

    private final List<String> parameterNames;
    private final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer;
    private final String methodName;


    public JavaDocVisitor(final String methodName,
            final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer) {
        this.methodName = methodName;
        this.stepDescriptorToStringTransformer = stepDescriptorToStringTransformer;
        this.description = new StringBuilder();
        this.example = new StringBuilder();
        this.expression = new StringBuilder();
        this.parameterNames = new ArrayList<String>();
    }


    @Override
    public void endVisit(final Javadoc node) {
        super.endVisit(node);
        if (isCorrectMethod(node.getParent())) {
            for (final Object tag : node.tags()) {
                final TagElement tagElement = (TagElement) tag;
                if (tagElement.getTagName() == null) {
                    appendFragmentsTo(tagElement, description);
                }

                else if (tagElement.getTagName().trim().startsWith("@example")) {
                    appendFragmentsTo(tagElement, example);
                }
            }
        }
    }


    @Override
    public void endVisit(final SingleMemberAnnotation node) {
        super.endVisit(node);

        if (isCorrectMethod(node.getParent())) {
            if ("Step".equals(node.getTypeName().toString())) {
                expression.append(node.getValue());
            }
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void endVisit(final MethodDeclaration node) {
        // TODO Auto-generated method stub
        super.endVisit(node);

        if (isCorrectMethod(node)) {
            final List<Object> parameterProperties = ((List<Object>) node
                    .getStructuralProperty(MethodDeclaration.PARAMETERS_PROPERTY));
            for (final Object prop : parameterProperties) {
                if (prop instanceof SingleVariableDeclaration) {
                    this.parameterNames.add(((SingleVariableDeclaration) prop).getName().toString());
                }
            }
        }
    }


    public String parsedJavaDoc() {

        if (isStepDescriptorDefined()) {
            final StepDescriptor descriptor = new StepDescriptor();
            descriptor.setDescription(description.toString());
            descriptor.setExample(example.toString());
            descriptor.setExpression(buildExpression());

            return stepDescriptorToStringTransformer.from(descriptor);
        }

        return description.toString();
    }


    private boolean isStepDescriptorDefined() {
        return description.length() > 0 && expression.length() > 0;
    }


    private void appendFragmentsTo(final TagElement tagElement, final StringBuilder sb) {
        for (final Object fragment : tagElement.fragments()) {
            if (fragment instanceof TextElement) {
                sb.append(((TextElement) fragment).getText());
                sb.append("\n");
            }
        }
    }


    private String buildExpression() {
        String expr = expression.toString();
        for (final String parameter : parameterNames) {
            expr = expr.replaceFirst("\\([^\\)]*\\)", "<" + parameter + ">");
        }
        expr = expr.replaceAll("\\?", "");
        expr = expr.replaceAll("\\\\", "");

        if (expr.startsWith("\"")) {
            expr = expr.substring(1, expr.length() - 1);
        }
        return expr;
    }


    private boolean isCorrectMethod(final ASTNode node) {
        if (node instanceof MethodDeclaration) {
            final MethodDeclaration method = (MethodDeclaration) node;
            return methodName.equals(method.getName().toString());
        }
        return false;
    }
}
