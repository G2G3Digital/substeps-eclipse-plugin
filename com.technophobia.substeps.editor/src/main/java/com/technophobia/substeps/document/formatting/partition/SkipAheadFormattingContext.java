package com.technophobia.substeps.document.formatting.partition;

import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

public class SkipAheadFormattingContext extends PartitionedFormattingContext {

    private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;
    private final TypedPosition[] positions;
    private final int skipAheadPosition;


    public SkipAheadFormattingContext(final TypedPosition[] positions, final int currentPosition,
            final int skipAheadPosition, final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
        super(positions, currentPosition, contentTypeDefinitionFactory);
        this.positions = positions;
        this.skipAheadPosition = skipAheadPosition;
        this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
    }


    @Override
    public ContentTypeDefinition currentContentType() {
        final ContentTypeDefinition currentTypeDefinition = super.currentContentType();
        final ContentTypeDefinition skipAheadTypeDefinition = contentTypeDefinitionFactory
                .contentTypeDefintionByName(positions[skipAheadPosition].getType());
        return new ContentTypeDefinition() {

            @Override
            public String prefixText() {
                return currentTypeDefinition.prefixText();
            }


            @Override
            public IPredicateRule partitionRule() {
                return currentTypeDefinition.partitionRule();
            }


            @Override
            public boolean isOptional() {
                return currentTypeDefinition.isOptional();
            }


            @Override
            public String id() {
                return currentTypeDefinition.id();
            }


            @Override
            public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
                return skipAheadTypeDefinition.formattingStrategy(formattingContextSupplier);
            }


            @Override
            public IRule damageRepairerRule(final ColourManager colourManager) {
                return currentTypeDefinition.damageRepairerRule(colourManager);
            }
        };
    }


    @Override
    protected int nextPosition() {
        return skipAheadPosition + 1;
    }
}
