/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.content.feature;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.NullContentTypeDefinition;

/**
 * Implementation of {@link ContentTypeDefinitionFactory} returning
 * {@link FeatureContentTypeDefinition} items
 */
public class FeatureContentTypeDefinitionFactory implements ContentTypeDefinitionFactory {

    private static final ContentTypeDefinition DEFAULT_CONTENT_TYPEDEFINITION = new NullContentTypeDefinition();


    @Override
    public ContentTypeDefinition contentTypeDefintionByName(final String contentTypeName) {
        for (final ContentTypeDefinition contentTypeDefinition : FeatureContentTypeDefinition.values()) {
            if (contentTypeName.equals(contentTypeDefinition.id())) {
                return contentTypeDefinition;
            }
        }
        return DEFAULT_CONTENT_TYPEDEFINITION;
    }


    @Override
    public ContentTypeDefinition[] contentTypeDefinitions() {
        return FeatureContentTypeDefinition.values();
    }
}
