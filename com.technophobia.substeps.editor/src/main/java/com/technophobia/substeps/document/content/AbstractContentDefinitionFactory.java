/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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
