package com.technophobia.substeps.document.content.assist.feature;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.substeps.document.content.assist.CompletionProvidedContentProcessor;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.render.ParameterisedStepImplementationRenderer;
import com.technophobia.substeps.step.ContextualSuggestionManager;
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
    private final ContextualSuggestionManager suggestionManager;


    public StepImplementationProcessorSupplier(final IWorkbenchPartSite site,
            final ContextualSuggestionManager suggestionManager) {
        this.site = site;
        this.suggestionManager = suggestionManager;
    }


    @Override
    public IContentAssistProcessor get() {
        return new CompletionProvidedContentProcessor(new StepImplementationProposalProvider(site,
                new ParameterisedStepImplementationRenderer(), new ProjectToSyntaxTransformer(), suggestionManager));
    }

}
