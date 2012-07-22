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


    public MultiLineFixedIndentFormattingStrategy(final String initialLineIndent, final String subsequentLineIndent) {
        this.initialLineIndent = initialLineIndent;
        this.subsequentLineIndent = subsequentLineIndent;
    }


    @Override
    public String format(final String content, final boolean isLineStart, final String indentation,
            final int[] positions) {

        final String[] lines = content.split("\n");

        if (lines.length > 0) {
            final StringBuffer sb = new StringBuffer();

            sb.append(initialLineIndent + lines[0].trim());
            if (lines.length > 1) {
                sb.append("\n");
            }

            for (int i = 1; i < lines.length; i++) {
                final String trimmed = lines[i].trim();
                if (trimmed.length() == 0) {
                    sb.append("");
                } else {
                    sb.append(subsequentLineIndent + trimmed);
                    // don't add new line to last line as its taken care of by
                    // tailingNewLines()
                    if (i < lines.length - 1) {
                        sb.append("\n");
                    }
                }
            }

            tailingNewLines(content, sb);
            return sb.toString();
        }
        return "";
    }


    private void tailingNewLines(final String content, final StringBuffer sb) {
        for (int i = content.length() - 1; i >= 0 && content.charAt(i) == '\n'; i--) {
            sb.append("\n");
        }
    }

}