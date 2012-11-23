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
