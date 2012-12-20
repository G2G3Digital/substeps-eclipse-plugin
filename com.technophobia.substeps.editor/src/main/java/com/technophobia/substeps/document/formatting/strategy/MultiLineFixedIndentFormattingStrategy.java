/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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
