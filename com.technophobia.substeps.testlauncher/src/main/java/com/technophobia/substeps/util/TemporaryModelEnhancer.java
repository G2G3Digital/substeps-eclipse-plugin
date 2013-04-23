package com.technophobia.substeps.util;

import org.eclipse.core.runtime.CoreException;

import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Predicate;

public class TemporaryModelEnhancer<Model> {

    private final Callback1<Model> enhancementCallback;
    private final Callback1<Model> detractionCallback;
    private final ModelOperation<Model> operation;
    private final Predicate<Model> enhancementRequiredPredicate;


    public TemporaryModelEnhancer(final Callback1<Model> enhancementCallback,
            final Callback1<Model> detractionCallback, final ModelOperation<Model> operation,
            final Predicate<Model> enhancementRequiredPredicate) {
        this.enhancementCallback = enhancementCallback;
        this.detractionCallback = detractionCallback;
        this.operation = operation;
        this.enhancementRequiredPredicate = enhancementRequiredPredicate;
    }


    public void doOperationFor(final Model model) throws CoreException {

        final boolean requireEnhancement = enhancementRequiredPredicate.forModel(model);

        if (requireEnhancement) {
            enhancementCallback.doCallback(model);
        }

        try {
            operation.doOperationOn(model);
        } finally {
            if (requireEnhancement) {
                detractionCallback.doCallback(model);
            }
        }
    }

}
