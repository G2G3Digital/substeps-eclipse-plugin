package com.technophobia.substeps.document.content;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractContentDefinitionFactory implements ContentTypeDefinitionFactory {

    private static final ContentTypeDefinition DEFAULT_CONTENT_TYPEDEFINITION = new NullContentTypeDefinition();

    private final Map<String, ContentTypeDefinition> contentDefinitionsByName;


    public AbstractContentDefinitionFactory(final ContentTypeDefinition... definitions) {
        contentDefinitionsByName = new HashMap<String, ContentTypeDefinition>();
        for (final ContentTypeDefinition definition : definitions) {
            contentDefinitionsByName.put(definition.id(), definition);
        }
    }


    @Override
    public String[] contentTypeIds() {
        return contentDefinitionsByName.keySet().toArray(new String[contentDefinitionsByName.size()]);
    }


    @Override
    public ContentTypeDefinition contentTypeDefintionByName(final String contentTypeName) {
        return contentDefinitionsByName.containsKey(contentTypeName) ? contentDefinitionsByName.get(contentTypeName)
                : DEFAULT_CONTENT_TYPEDEFINITION;
    }


    @Override
    public ContentTypeDefinition[] contentTypeDefinitions() {
        return contentDefinitionsByName.values().toArray(new ContentTypeDefinition[contentDefinitionsByName.size()]);
    }
}
