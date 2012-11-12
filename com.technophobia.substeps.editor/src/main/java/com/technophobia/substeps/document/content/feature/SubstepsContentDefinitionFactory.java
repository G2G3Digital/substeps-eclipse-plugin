package com.technophobia.substeps.document.content.feature;

import com.technophobia.substeps.document.content.AbstractContentDefinitionFactory;
import com.technophobia.substeps.document.content.feature.definition.CommentContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.DefineContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.StepContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.definition.TagContentTypeDefinition;

public class SubstepsContentDefinitionFactory extends AbstractContentDefinitionFactory {

    public SubstepsContentDefinitionFactory() {
        super(new DefineContentTypeDefinition(), //
                new CommentContentTypeDefinition(), //
                new TagContentTypeDefinition(), //
                new StepContentTypeDefinition());
    }
}
