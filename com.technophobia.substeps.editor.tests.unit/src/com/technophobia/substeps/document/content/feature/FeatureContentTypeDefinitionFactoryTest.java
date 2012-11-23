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
