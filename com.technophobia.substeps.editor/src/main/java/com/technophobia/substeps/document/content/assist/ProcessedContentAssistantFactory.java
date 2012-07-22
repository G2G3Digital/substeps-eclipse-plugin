package com.technophobia.substeps.document.content.assist;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinition;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Implementation of {@link ContentAssistantFactory} that adds a supplied
 * {@link IContentAssistProcessor} to a new {@link IContentAssistant}, followed
 * by decorating it using {@link Callback1}'s
 * 
 * @author sforbes
 * 
 */
public class ProcessedContentAssistantFactory implements ContentAssistantFactory {

    private final Supplier<IContentAssistProcessor> processorSupplier;
    private final Callback1<IContentAssistant>[] contentAssistantDecorators;


    public ProcessedContentAssistantFactory(final Supplier<IContentAssistProcessor> processorSupplier,
            final Callback1<IContentAssistant>... contentAssistantDecorators) {
        this.processorSupplier = processorSupplier;
        this.contentAssistantDecorators = contentAssistantDecorators;
    }


    @Override
    public IContentAssistant createContentAssist() {

        final ContentAssistant assistant = new ContentAssistant();
        final IContentAssistProcessor processor = processorSupplier.get();

        for (final ContentTypeDefinition contentTypeDefinition : FeatureContentTypeDefinition.values()) {
            assistant.setContentAssistProcessor(processor, contentTypeDefinition.id());
        }

        for (final Callback1<IContentAssistant> callback : contentAssistantDecorators) {
            callback.doCallback(assistant);
        }

        return assistant;
    }

}
