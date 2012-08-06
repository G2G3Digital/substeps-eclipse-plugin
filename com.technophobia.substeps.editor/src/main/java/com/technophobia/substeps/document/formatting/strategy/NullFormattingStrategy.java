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
package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

/**
 * Null Object pattern for {@link IFormattingStrategy} interface
 * 
 * @author sforbes
 * 
 */
public class NullFormattingStrategy implements IFormattingStrategy {

    protected static final String lineSeparator = System.getProperty("line.separator");


    public NullFormattingStrategy() {
        super();
    }


    @Override
    public void formatterStarts(final String initialIndentation) {
        // no-op
    }


    @Override
    public String format(final String content, final boolean isLineStart, final String indentation,
            final int[] positions) {
        return content;
    }


    @Override
    public void formatterStops() {
        // no-op
    }

}
