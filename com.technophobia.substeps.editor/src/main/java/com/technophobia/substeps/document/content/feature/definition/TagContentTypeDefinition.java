package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.OptionalUnitPrefixFormattingStrategy;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.supplier.Supplier;

public class TagContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__feature_tag";
    public static final String PREFIX_TEXT = "Tags:";
    private static final String[] VALID_PROCEEDING_CONTENT_TYPES = { CommentContentTypeDefinition.PREFIX_TEXT,
            FeatureContentTypeDefinition.PREFIX_TEXT, ScenarioContentTypeDefinition.PREFIX_TEXT,
            ScenarioOutlineContentTypeDefinition.PREFIX_TEXT };


    public TagContentTypeDefinition() {
        super(CONTENT_TYPE_ID, PREFIX_TEXT, true);
    }


    @Override
    public IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier) {
        return paragraphRule(PREFIX_TEXT, id(), false, VALID_PROCEEDING_CONTENT_TYPES);
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return paragraphRule(PREFIX_TEXT, colourToken(FeatureColour.GREEN, colourManager), true,
                VALID_PROCEEDING_CONTENT_TYPES);
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new OptionalUnitPrefixFormattingStrategy(formattingContextSupplier);
    }
}
