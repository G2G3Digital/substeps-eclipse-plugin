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
package com.technophobia.substeps.document.content;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Representation of a content type allowed in the editor
 * 
 * @author sforbes
 * 
 */
public interface ContentTypeDefinition {

    /**
     * Unique id of the content type
     * 
     * @return id
     */
    String id();


    /**
     * Text that will be visible in the editor for this content type
     * 
     * @return prefixText
     */
    String prefixText();


    /**
     * Is this item optional in the editor
     * 
     * @return true if content type is optional, false otherwise
     */
    boolean isOptional();


    /**
     * Return the {@link PredicateRule} associated with this content type
     * 
     * @param partitionContext
     * 
     * @return PredicateRule
     */
    IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier);


    /**
     * Return the DamageRepairer {@link IRule} associated with this content type
     * 
     * @return DamageRepairer Rule
     */
    IRule damageRepairerRule(ColourManager colourManager, final Supplier<PartitionContext> partitionContextSupplier);


    /**
     * Returns the {@link IFormattingStrategy} based on the supplied
     * {@link FormattingContext}
     * 
     * @param formattingContextSupplier
     *            Supplies the formatting context
     * @return IFormattingStrategy
     */
    IFormattingStrategy formattingStrategy(Supplier<FormattingContext> formattingContextSupplier);
}
