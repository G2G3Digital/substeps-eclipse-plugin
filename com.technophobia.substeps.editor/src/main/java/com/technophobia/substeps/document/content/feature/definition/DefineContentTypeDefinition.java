package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.MultiLineFixedIndentFormattingStrategy;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.supplier.Supplier;

public class DefineContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__substeps_define";
    public static final String PREFIX_TEXT = "Define:";


    public DefineContentTypeDefinition() {
        super(CONTENT_TYPE_ID, PREFIX_TEXT, false);
    }


    @Override
    public IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier) {
        return paragraphRule(PREFIX_TEXT, id(), true, CommentContentTypeDefinition.PREFIX_TEXT,
                DefineContentTypeDefinition.PREFIX_TEXT);
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return fixedWordRule(PREFIX_TEXT, colourToken(FeatureColour.BLUE, colourManager));
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new MultiLineFixedIndentFormattingStrategy("", "\t", formattingContextSupplier);
    }
}
