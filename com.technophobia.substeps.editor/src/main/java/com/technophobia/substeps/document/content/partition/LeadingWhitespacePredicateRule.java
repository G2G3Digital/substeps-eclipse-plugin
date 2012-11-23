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
package com.technophobia.substeps.document.content.partition;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WordPatternRule;

public final class LeadingWhitespacePredicateRule extends WordPatternRule {

    private static class DummyDetector implements IWordDetector {

        /*
         * @see IWordDetector#isWordStart
         */
        @Override
        public boolean isWordStart(final char c) {
            return false;
        }


        /*
         * @see IWordDetector#isWordPart
         */
        @Override
        public boolean isWordPart(final char c) {
            return false;
        }
    }


    public LeadingWhitespacePredicateRule(final IToken token, final String whitespace) {
        super(new DummyDetector(), whitespace, "dummy", token); //$NON-NLS-1$
        setColumnConstraint(0);
    }


    /*
     * @see
     * org.eclipse.jface.text.rules.WordPatternRule#endSequenceDetected(org.
     * eclipse.jface.text.rules.ICharacterScanner)
     */
    @Override
    protected boolean endSequenceDetected(final ICharacterScanner scanner) {
        int c;
        do {
            c = scanner.read();
        } while (Character.isWhitespace((char) c));

        scanner.unread();

        return true;
    }
}
