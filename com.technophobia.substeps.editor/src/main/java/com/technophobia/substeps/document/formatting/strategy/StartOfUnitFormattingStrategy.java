package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Implementation of {@link IFormattingStrategy} that defers formatting to a
 * different {@link IFormattingStrategy}, wrapping it with relevant newlines
 * 
 * @author sforbes
 * 
 */
public class StartOfUnitFormattingStrategy extends DefaultFormattingStrategy {

    private final IFormattingStrategy formattingStrategy;
    private final Supplier<FormattingContext> formattingContextSupplier;
    private final int numLeadingLines;
    private final int numTrailingLines;


    public StartOfUnitFormattingStrategy(final int numLeadingLines, final int numTrailingLines,
            final Supplier<FormattingContext> formattingContextSupplier, final IFormattingStrategy formattingStrategy) {
        this.numLeadingLines = numLeadingLines;
        this.numTrailingLines = numTrailingLines;
        this.formattingContextSupplier = formattingContextSupplier;
        this.formattingStrategy = formattingStrategy;

    }


    @Override
    public String format(final String content, final boolean isLineStart, final String indentation,
            final int[] positions) {
        FeatureEditorPlugin.instance().info(
                "Formatting line: " + content + ", isLineStart: " + isLineStart + ", indentation: " + indentation);
        /*
         * if (isLineStart) { final FormattingContext formattingContext =
         * formattingContextSupplier.get(); if
         * (formattingContext.hasPreviousContent()) { final FormattingContext
         * previousContext = formattingContext.previousContentContext(); if
         * (!previousContext.currentContentType().isOptional()) { prefixNewLine
         * = true; } } }
         */
        final String formattedContent = removeLeadingNewlines(formattingStrategy.format(content.trim(), isLineStart,
                indentation, positions));

        final StringBuffer sb = new StringBuffer();
        addNewlinesTo(numLeadingLines, hasPreviousContent(), sb);
        sb.append(formattedContent);
        addNewlinesTo(numTrailingLines, hasNextContent() && !isOptional(), sb);
        return sb.toString();
    }


    private String removeLeadingNewlines(final String content) {
        if (content.length() > 0 && content.startsWith(NEWLINE)) {
            return removeLeadingNewlines(content.substring(NEWLINE.length()));
        }

        return content;
    }


    private boolean hasPreviousContent() {
        return formattingContextSupplier.get().hasPreviousContent()
                && !formattingContextSupplier.get().inspectPreviousContentType().isOptional();
    }


    private boolean hasNextContent() {
        return formattingContextSupplier.get().hasMoreContent();
    }


    private boolean isOptional() {
        return formattingContextSupplier.get().currentContentType().isOptional();
    }


    private void addNewlinesTo(final int numLines, final boolean hasOtherContent, final StringBuffer sb) {
        if (hasOtherContent) {
            for (int i = 0; i < numLines; i++) {
                sb.append(NEWLINE);
            }
        }
    }
}
