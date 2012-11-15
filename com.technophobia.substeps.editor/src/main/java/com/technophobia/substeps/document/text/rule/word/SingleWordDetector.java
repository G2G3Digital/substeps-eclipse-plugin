package com.technophobia.substeps.document.text.rule.word;

import org.eclipse.jface.text.rules.IWordDetector;

public class SingleWordDetector implements IWordDetector {

    private final String word;


    public SingleWordDetector(final String word) {
        this.word = word;
    }


    /**
     * Only really interested in the keyword, so only pick words which start
     * with the same character
     */
    @Override
    public boolean isWordStart(final char c) {
        return c == word.charAt(0);
    }


    /**
     * "Words" may contain any non-whitespace characters
     */
    @Override
    public boolean isWordPart(final char c) {
        return !Character.isWhitespace(c);
    }
}
