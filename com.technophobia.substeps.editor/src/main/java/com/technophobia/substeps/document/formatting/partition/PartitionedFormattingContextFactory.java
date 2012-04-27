package com.technophobia.substeps.document.formatting.partition;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;

public class PartitionedFormattingContextFactory implements FormattingContextFactory {

	private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	public PartitionedFormattingContextFactory(final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;

	}

	@Override
	public FormattingContext createFor(final IDocument document, final IRegion region) {
		return new PartitionedFormattingContext(document, region, contentTypeDefinitionFactory);
	}

}
