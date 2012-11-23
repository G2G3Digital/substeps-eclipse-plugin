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
package com.technophobia.substeps.document.text.rule;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

/**
 * Extention of {@link MultiLineRule} in which the end sequence may contain
 * whitespace that should be discounted when scanning for it
 * 
 * @author sforbes
 * 
 */
public class MultiLineFragmentedSequenceRule extends MultiLineRule {

    private static final char[] WHITESPACE_CHARS = new char[] { ' ', '\r', '\n', '\t' };


    public MultiLineFragmentedSequenceRule(final String startString, final String endSequence, final IToken token) {
        super(startString, endSequence, token, (char) 0, true);
    }


    @Override
    protected boolean sequenceDetected(final ICharacterScanner scanner, final char[] sequence, final boolean eofAllowed) {
        int i = 1;
        while (i < sequence.length) {
            final int c = scanner.read();
            if (c == ICharacterScanner.EOF && eofAllowed) {
                return true;
            } else if (c == sequence[i]) {
                i++;
            } else if (whitespaceChar(c)) {
                // no-op - don't fail yet, but likewise don't increment i
            } else {
                // Non-matching character detected, rewind the scanner back to
                // the start.
                // Do not unread the first character.
                scanner.unread();
                for (int j = i - 1; j > 0; j--)
                    scanner.unread();
                return false;
            }
        }

        return true;
    }


    /**
     * Determine if character is a whitespace character
     * 
     * @param c
     *            the character under test
     * @return true if c is whitespace, false otherwise
     */
    private boolean whitespaceChar(final int c) {
        for (final char whitespaceChar : WHITESPACE_CHARS) {
            if (whitespaceChar == c)
                return true;
        }
        return false;
    }
}
