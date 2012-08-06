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