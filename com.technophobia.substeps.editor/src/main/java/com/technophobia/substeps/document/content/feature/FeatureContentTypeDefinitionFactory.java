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

import com.technophobia.substeps.document.content.AbstractContentDefinitionFactory;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.feature.definition.BackgroundContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.CommentContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.FeatureContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleRowContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioOutlineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.StepContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.TagContentTypeDefinition;

/**
 * Implementation of {@link ContentTypeDefinitionFactory} returning
 * {@link FeatureContentTypeDefinition} items
 */
public class FeatureContentTypeDefinitionFactory extends AbstractContentDefinitionFactory {

    public FeatureContentTypeDefinitionFactory() {
        super(new FeatureContentTypeDefinition(), //
                new BackgroundContentTypeDefinition(), //
                new CommentContentTypeDefinition(), //
                new TagContentTypeDefinition(), //
                new ScenarioContentTypeDefinition(), //
                new ScenarioOutlineContentTypeDefinition(), //
                new ScenarioExampleContentTypeDefinition(), //
                new ScenarioExampleRowContentTypeDefinition(), //
                new StepContentTypeDefinition());
        // new GivenContentTypeDefinition(), //
        // new WhenContentTypeDefinition(), //
        // new ThenContentTypeDefinition(), //
        // new AndContentTypeDefinition());
    }
}
