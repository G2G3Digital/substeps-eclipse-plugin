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

import java.util.HashMap;
import java.util.Map;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.NullContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.AndContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.BackgroundContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.CommentContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.DefineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.FeatureContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.GivenContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleRowContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioOutlineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.TagContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ThenContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.WhenContentTypeDefinition;

/**
 * Implementation of {@link ContentTypeDefinitionFactory} returning
 * {@link FeatureContentTypeDefinition} items
 */
public class FeatureContentTypeDefinitionFactory implements ContentTypeDefinitionFactory {

    private static final ContentTypeDefinition DEFAULT_CONTENT_TYPEDEFINITION = new NullContentTypeDefinition();

    private final Map<String, ContentTypeDefinition> contentDefinitionsByName;


    public FeatureContentTypeDefinitionFactory() {
        contentDefinitionsByName = new HashMap<String, ContentTypeDefinition>();
        addDefinition(new FeatureContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new BackgroundContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new CommentContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new TagContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new ScenarioContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new ScenarioOutlineContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new ScenarioExampleContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new ScenarioExampleRowContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new GivenContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new WhenContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new ThenContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new AndContentTypeDefinition(), contentDefinitionsByName);
        addDefinition(new DefineContentTypeDefinition(), contentDefinitionsByName);
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


    private void addDefinition(final ContentTypeDefinition contentTypeDefinition,
            final Map<String, ContentTypeDefinition> definitionMap) {
        definitionMap.put(contentTypeDefinition.id(), contentTypeDefinition);
    }
}
