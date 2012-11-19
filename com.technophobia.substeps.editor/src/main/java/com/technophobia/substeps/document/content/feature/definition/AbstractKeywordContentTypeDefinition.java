package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.supplier.Supplier;

public abstract class AbstractKeywordContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    private final String prefixText;


    public AbstractKeywordContentTypeDefinition(final String id, final String prefixText, final boolean optional) {
        super(id, prefixText, optional);
        this.prefixText = prefixText;
    }


    @Override
    public IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier) {
        return singleLineWithTrailingCommentRule(prefixText, id());
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager,
            final Supplier<PartitionContext> partitionContextSupplier) {
        return fixedWordRule(prefixText, colourToken(FeatureColour.PINK, colourManager));
    }

}
