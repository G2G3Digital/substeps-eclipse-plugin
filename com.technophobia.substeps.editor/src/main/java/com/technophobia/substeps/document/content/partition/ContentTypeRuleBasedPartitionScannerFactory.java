package com.technophobia.substeps.document.content.partition;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.partition.PartitionScannerFactory;

public class ContentTypeRuleBasedPartitionScannerFactory implements
		PartitionScannerFactory {

	private final String[] legalContentTypes;
	private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	public ContentTypeRuleBasedPartitionScannerFactory(
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
		this.legalContentTypes = idsFrom(contentTypeDefinitionFactory);
	}

	@Override
	public IPartitionTokenScanner createScanner() {
		return new ContentTypeRuleBasedPartitionScanner(
				contentTypeDefinitionFactory);
	}

	@Override
	public String[] legalContentTypes() {
		return legalContentTypes;
	}

	private String[] idsFrom(
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		final ContentTypeDefinition[] contentTypeDefinitions = contentTypeDefinitionFactory
				.contentTypeDefinitions();

		final String[] ids = new String[contentTypeDefinitions.length];
		int i = 0;
		for (final ContentTypeDefinition contentTypeDefinition : contentTypeDefinitions) {
			ids[i] = contentTypeDefinition.id();
			i++;
		}

		return ids;
	}
}
