package com.technophobia.substeps.document.content;

public interface ContentTypeDefinitionFactory {

	ContentTypeDefinition[] contentTypeDefinitions();

	ContentTypeDefinition contentTypeDefintionByName(String contentTypeName);
}
