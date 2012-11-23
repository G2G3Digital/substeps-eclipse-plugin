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
