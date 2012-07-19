package com.technophobia.substeps.document.content.assist;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.FeatureContentTypeDefinition;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Supplier;

public class ProcessedContentAssistantFactory implements ContentAssistantFactory {

    private final Supplier<IContentAssistProcessor> processorSupplier;
    private final String contentType;
    private final Callback1<IContentAssistant>[] contentAssistantDecorators;


    public ProcessedContentAssistantFactory(final String contentType,
            final Supplier<IContentAssistProcessor> processorSupplier,
            final Callback1<IContentAssistant>... contentAssistantDecorators) {
        this.contentType = contentType;
        this.processorSupplier = processorSupplier;
        this.contentAssistantDecorators = contentAssistantDecorators;
    }


    @Override
    public IContentAssistant contentAssist() {

        final ContentAssistant assistant = new ContentAssistant();
        IContentAssistProcessor processor = processorSupplier.get();
        
        for(ContentTypeDefinition contentTypeDefinition : FeatureContentTypeDefinition.values()){
        	assistant.setContentAssistProcessor(processor, contentTypeDefinition.id());
        }

        for (final Callback1<IContentAssistant> callback : contentAssistantDecorators) {
            callback.doCallback(assistant);
        }

        return assistant;
    }

}
