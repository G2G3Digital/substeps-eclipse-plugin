package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.supplier.Transformer;

public class StepDescriptorToStringTransformer implements Transformer<StepDescriptor, String> {

    @Override
    public String from(final StepDescriptor stepDescriptor) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Expression: ");
        sb.append(stepDescriptor.getExpression());
        if (stepDescriptor.getExample() != null && stepDescriptor.getExample().length() > 0) {
            sb.append("\n");
            sb.append("Example: ");
            sb.append(stepDescriptor.getExample());
        }
        sb.append("\n\n");
        sb.append(stepDescriptor.getDescription());
        return sb.toString();
    }

}
