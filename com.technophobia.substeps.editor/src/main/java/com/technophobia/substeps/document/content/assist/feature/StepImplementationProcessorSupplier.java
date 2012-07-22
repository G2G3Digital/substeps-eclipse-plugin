package com.technophobia.substeps.document.content.assist.feature;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.substeps.document.content.assist.CompletionProvidedContentProcessor;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.render.ParameterisedStepImplementationRenderer;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Supplies {@link IContentAssistProcessor} based on supplied
 * {@link StepImplementation} that it looks up on the classpath
 * 
 * @author sforbes
 * 
 */
public class StepImplementationProcessorSupplier implements Supplier<IContentAssistProcessor> {

    private final IWorkbenchPartSite site;


    public StepImplementationProcessorSupplier(final IWorkbenchPartSite site) {
        this.site = site;
    }


    @Override
    public IContentAssistProcessor get() {
        return new CompletionProvidedContentProcessor(new StepImplementationProposalProvider(site,
                new ParameterisedStepImplementationRenderer(), new SiteToSyntaxTransformer()));
    }

}
