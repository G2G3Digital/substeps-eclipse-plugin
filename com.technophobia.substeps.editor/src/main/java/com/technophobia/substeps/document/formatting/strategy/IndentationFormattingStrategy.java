package com.technophobia.substeps.document.formatting.strategy;

import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Formatting Strategy that indents the content value
 * 
 * @author sforbes
 * 
 */
public abstract class IndentationFormattingStrategy extends DefaultFormattingStrategy {

    private final Supplier<FormattingContext> formattingContextSupplier;


    public IndentationFormattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        this.formattingContextSupplier = formattingContextSupplier;
    }


    @Override
    public void formatterStarts(final String initialIndentation) {
        super.formatterStarts(initialIndentation);
    }


    @Override
    public String format(final String content, final boolean isLineStart, final String indentation,
            final int[] positions) {

        final String indentedContent = indent() + content.trim();

        return formattingContextSupplier.get().hasPreviousContent() ? NEWLINE + indentedContent : indentedContent;
    }


    /**
     * What indent should the content have
     * 
     * @return The indent in string format
     */
    protected abstract String indent();
}
