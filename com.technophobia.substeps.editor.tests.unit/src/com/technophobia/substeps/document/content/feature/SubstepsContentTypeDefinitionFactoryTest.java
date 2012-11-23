package com.technophobia.substeps.document.content.feature;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.NullContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.CommentContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.DefineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.StepContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.TagContentTypeDefinition;

public class SubstepsContentTypeDefinitionFactoryTest {

    private ContentTypeDefinitionFactory contentTypeDefinitionFactory;


    @Before
    public void initialiseFactory() {
        this.contentTypeDefinitionFactory = new SubstepsContentDefinitionFactory();
    }


    @Test
    public void canGetAllSubstepsContentTypeDefinitionsByName() {
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__substeps_define") instanceof DefineContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_tag") instanceof TagContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_comment") instanceof CommentContentTypeDefinition);
        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("__feature_step") instanceof StepContentTypeDefinition);
    }


    @Test
    public void returnsNullDefinitionWhenInvalidTypeRequested() {

        assertTrue(contentTypeDefinitionFactory.contentTypeDefintionByName("invalid_type") instanceof NullContentTypeDefinition);
    }
}
