package com.technophobia.substeps.document.formatting.strategy;

import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Sub-class of {@link IndentationFormattingStrategy} providing a fixed indent
 * 
 * @author sforbes
 * 
 */
public class FixedIndentFormattingStrategy extends IndentationFormattingStrategy {

    private final String indent;


    public FixedIndentFormattingStrategy(final String indent,
            final Supplier<FormattingContext> formattingContextSupplier) {
        super(formattingContextSupplier);
        this.indent = indent;
    }


    @Override
    protected String indent() {
        return indent;
    }

}
