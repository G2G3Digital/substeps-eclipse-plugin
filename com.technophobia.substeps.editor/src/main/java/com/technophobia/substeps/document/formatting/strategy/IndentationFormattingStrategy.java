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
