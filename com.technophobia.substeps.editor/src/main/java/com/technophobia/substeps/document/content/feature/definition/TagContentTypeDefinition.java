package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.OptionalUnitPrefixFormattingStrategy;
import com.technophobia.substeps.supplier.Supplier;

public class TagContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__feature_tag";


    public TagContentTypeDefinition() {
        super(CONTENT_TYPE_ID, true);
    }


    @Override
    public IPredicateRule partitionRule() {
        return singleLineRule("Tags:", id());
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return singleLineRule("Tags:", colourToken(FeatureColour.GREEN, colourManager));
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new OptionalUnitPrefixFormattingStrategy(formattingContextSupplier);
    }
}
