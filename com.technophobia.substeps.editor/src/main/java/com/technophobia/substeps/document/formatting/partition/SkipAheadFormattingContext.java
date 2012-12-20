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
package com.technophobia.substeps.document.formatting.partition;

import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.partition.PartitionContext;
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
            public IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier) {
                return currentTypeDefinition.partitionRule(partitionContextSupplier);
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
            public IRule damageRepairerRule(final ColourManager colourManager,
                    final Supplier<PartitionContext> partitionContextSupplier) {
                return currentTypeDefinition.damageRepairerRule(colourManager, partitionContextSupplier);
            }
        };
    }


    @Override
    protected int nextPosition() {
        return skipAheadPosition + 1;
    }
}
