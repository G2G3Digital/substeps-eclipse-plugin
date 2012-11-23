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
package com.technophobia.substeps.document.content.feature;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.NullContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.BackgroundContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.CommentContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.FeatureContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioExampleRowContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.ScenarioOutlineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.StepContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.TagContentTypeDefinition;

public class FeatureContentTypeDefinitionFactoryTest {

    private ContentTypeDefinitionFactory contentTypeDefinitionFactory;


    @Before
    public void initialiseFactory() {
        this.contentTypeDefinitionFactory = new FeatureContentTypeDefinitionFactory();
    }


    @Test
    public void canGetAllFeatureContentTypeDefinitionsByName() {
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_feature") instanceof FeatureContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_tag") instanceof TagContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_comment") instanceof CommentContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_background") instanceof BackgroundContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_scenario") instanceof ScenarioContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_scenario_outline") instanceof ScenarioOutlineContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_example") instanceof ScenarioExampleContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_example_row") instanceof ScenarioExampleRowContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_step") instanceof StepContentTypeDefinition);
    }


    @Test
    public void returnsNullDefinitionWhenInvalidTypeRequested() {

        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("invalid_type") instanceof NullContentTypeDefinition);
    }
}
