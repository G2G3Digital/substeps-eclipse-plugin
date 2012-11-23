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
