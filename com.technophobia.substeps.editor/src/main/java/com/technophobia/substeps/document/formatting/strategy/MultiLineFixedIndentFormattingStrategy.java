package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Implementation of {@link IFormattingStrategy} that formats multiple lines,
 * each with a specific indent
 * 
 * @author sforbes
 * 
 */
public class MultiLineFixedIndentFormattingStrategy extends DefaultFormattingStrategy {

    /**
     * Amount the 1st line should be indented
     */
    private final String initialLineIndent;

    /**
     * Amount subsequent lines should be indented
     */
    private final String subsequentLineIndent;

    private final Supplier<FormattingContext> formattingContextSupplier;


    public MultiLineFixedIndentFormattingStrategy(final String initialLineIndent, final String subsequentLineIndent,
            final Supplier<FormattingContext> formattingContextSupplier) {
        this.initialLineIndent = initialLineIndent;
        this.subsequentLineIndent = subsequentLineIndent;
        this.formattingContextSupplier = formattingContextSupplier;
    }


    @Override
    public String format(final String content, final boolean isLineStart, final String indentation,
            final int[] positions) {

        final String[] lines = content.split(NEWLINE);

        if (lines.length > 0) {
            final StringBuffer sb = new StringBuffer();
            leadingNewLines(sb);

            sb.append(initialLineIndent + lines[0].trim());

            for (int i = 1; i < lines.length; i++) {
                final String trimmed = lines[i].trim();
                if (trimmed.length() > 0) {
                    sb.append(NEWLINE);
                    sb.append(subsequentLineIndent + trimmed);
                }
            }

            // tailingNewLines(sb);
            return sb.toString();
        }
        return "";
    }


    private void leadingNewLines(final StringBuffer sb) {
        if (formattingContextSupplier.get().hasPreviousContent()
                && !formattingContextSupplier.get().inspectPreviousContentType().isOptional()) {
            sb.append(NEWLINE + NEWLINE);
        }
    }
}