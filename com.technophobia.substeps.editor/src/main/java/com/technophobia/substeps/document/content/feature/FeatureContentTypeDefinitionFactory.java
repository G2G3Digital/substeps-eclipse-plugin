package com.technophobia.substeps.document.content.feature;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;

public class FeatureContentTypeDefinitionFactory implements
		ContentTypeDefinitionFactory {

	@Override
	public ContentTypeDefinition contentTypeDefintionByName(
			final String contentTypeName) {
		for (final ContentTypeDefinition contentTypeDefinition : FeatureContentTypeDefinition
				.values()) {
			if (contentTypeName.equals(contentTypeDefinition.id())) {
				return contentTypeDefinition;
			}
		}
		throw new IllegalStateException("Could not find "
				+ FeatureContentTypeDefinition.class.getSimpleName()
				+ " with name " + contentTypeName);
	}

	@Override
	public ContentTypeDefinition[] contentTypeDefinitions() {
		return FeatureContentTypeDefinition.values();
	}
}
