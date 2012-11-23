package com.technophobia.substeps.document.content;

/**
 * Factory interface for {@link ContentTypeDefinition}
 * 
 * @author sforbes
 * 
 */
public interface ContentTypeDefinitionFactory {

    /**
     * All possible content type ids
     * 
     * @return
     */
    String[] contentTypeIds();


    /**
     * All {@link ContentTypeDefinition}s
     * 
     * @return ContentTypeDefinitions
     */
    ContentTypeDefinition[] contentTypeDefinitions();


    /**
     * Find {@link ContentTypeDefinition} by unique name
     * 
     * @param contentTypeName
     *            the unique identifying name
     * @return ContentTypeDefinition
     */
    ContentTypeDefinition contentTypeDefintionByName(String contentTypeName);
}
