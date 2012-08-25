package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.FixedIndentFormattingStrategy;
import com.technophobia.substeps.document.formatting.strategy.StartOfUnitFormattingStrategy;
import com.technophobia.substeps.supplier.Supplier;

public class BackgroundContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__feature_background";
    public static final String PREFIX_TEXT = "Background:";


    public BackgroundContentTypeDefinition() {
        super(CONTENT_TYPE_ID, PREFIX_TEXT, false);
    }


    @Override
    public IPredicateRule partitionRule() {
        return paragraphRule(PREFIX_TEXT, id(), true, TagContentTypeDefinition.PREFIX_TEXT,
                CommentContentTypeDefinition.PREFIX_TEXT, BackgroundContentTypeDefinition.PREFIX_TEXT,
                ScenarioContentTypeDefinition.PREFIX_TEXT, ScenarioOutlineContentTypeDefinition.PREFIX_TEXT,
                GivenContentTypeDefinition.PREFIX_TEXT, WhenContentTypeDefinition.PREFIX_TEXT,
                ThenContentTypeDefinition.PREFIX_TEXT);
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return fixedWordRule(PREFIX_TEXT, colourToken(FeatureColour.BLUE, colourManager));
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new StartOfUnitFormattingStrategy(1, 1, formattingContextSupplier, new FixedIndentFormattingStrategy(
                "  "));
    }
}
