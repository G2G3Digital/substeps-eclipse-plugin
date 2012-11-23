package com.technophobia.substeps.document.formatting.partition;

import org.eclipse.jface.text.TypedPosition;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;

/**
 * Creates a new {@link PartitionedFormattingContext}
 * 
 * @author sforbes
 * 
 */
public class PartitionedFormattingContextFactory implements FormattingContextFactory {

    private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;


    public PartitionedFormattingContextFactory(final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
        this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;

    }


    @Override
    public FormattingContext createFor(final TypedPosition[] positions, final int currentPosition) {
        return new PartitionedFormattingContext(positions, currentPosition, contentTypeDefinitionFactory);
    }

}
