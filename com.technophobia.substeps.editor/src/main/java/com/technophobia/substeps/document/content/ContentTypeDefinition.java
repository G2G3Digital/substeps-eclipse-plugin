/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.content;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.formatting.FormattingContext;
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
     * Is this item optional in the editor
     * 
     * @return true if content type is optional, false otherwise
     */
    boolean isOptional();


    /**
     * Return the {@link PredicateRule} associated with this content type
     * 
     * @return PredicateRule
     */
    IPredicateRule partitionRule();


    /**
     * Return the DamageRepairer {@link IRule} associated with this content type
     * 
     * @return DamageRepairer Rule
     */
    IRule damageRepairerRule(ColourManager colourManager);


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
