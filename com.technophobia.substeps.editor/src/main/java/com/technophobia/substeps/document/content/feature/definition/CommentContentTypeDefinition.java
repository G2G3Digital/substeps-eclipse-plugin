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
package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.OptionalUnitPrefixFormattingStrategy;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.document.text.rule.SingleLineWithTrailingCommentRule;
import com.technophobia.substeps.supplier.Supplier;

public class CommentContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__feature_comment";
    public static final String PREFIX_TEXT = SingleLineWithTrailingCommentRule.TRAILING_COMMENT_START;


    public CommentContentTypeDefinition() {
        super(CONTENT_TYPE_ID, PREFIX_TEXT, true);
    }


    @Override
    public IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier) {
        return singleLineRule(PREFIX_TEXT, id());
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager,
            final Supplier<PartitionContext> partitionContextSupplier) {
        return singleLineRule(PREFIX_TEXT, colourToken(FeatureColour.SLATE, colourManager));
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new OptionalUnitPrefixFormattingStrategy(formattingContextSupplier);
    }
}
