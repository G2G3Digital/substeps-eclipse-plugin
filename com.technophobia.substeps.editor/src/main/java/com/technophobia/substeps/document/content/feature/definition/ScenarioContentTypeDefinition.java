package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.FixedIndentFormattingStrategy;
import com.technophobia.substeps.document.formatting.strategy.StartOfUnitFormattingStrategy;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.supplier.Supplier;

public class ScenarioContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__feature_scenario";
    public static final String PREFIX_TEXT = "Scenario:";


    public ScenarioContentTypeDefinition() {
        super(CONTENT_TYPE_ID, PREFIX_TEXT, false);
    }


    @Override
    public IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier) {
        return singleLineWithTrailingCommentRule(PREFIX_TEXT, id());
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return fixedWordRule(PREFIX_TEXT, colourToken(FeatureColour.BLUE, colourManager));
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new StartOfUnitFormattingStrategy(2, 0, formattingContextSupplier, new FixedIndentFormattingStrategy(
                "  ", formattingContextSupplier));
    }

}
