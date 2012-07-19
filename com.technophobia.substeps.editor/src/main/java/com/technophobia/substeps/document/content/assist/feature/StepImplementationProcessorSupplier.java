package com.technophobia.substeps.document.content.assist.feature;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;

import com.technophobia.substeps.document.content.assist.CompletionProvidedContentProcessor;
import com.technophobia.substeps.runner.runtime.ClassLocator;
import com.technophobia.substeps.supplier.Supplier;

public class StepImplementationProcessorSupplier implements Supplier<IContentAssistProcessor> {

    private final String outputFolder;
    private final ClassLocator classLocator;


    public StepImplementationProcessorSupplier(final String outputFolder, final ClassLocator classLocator) {
        this.outputFolder = outputFolder;
        this.classLocator = classLocator;
    }


    @Override
    public IContentAssistProcessor get() {
        return new CompletionProvidedContentProcessor(
                new StepImplementationProposalProvider(outputFolder, classLocator));
    }

}
