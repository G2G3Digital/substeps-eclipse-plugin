package com.technophobia.substeps.document.text.rule.word;

import org.eclipse.jface.text.rules.IWordDetector;

public class MultipleChoiceSingleWordDetector implements IWordDetector {

    private final char[] firstChars;


    public MultipleChoiceSingleWordDetector(final String... words) {
        this.firstChars = firstLettersOf(words);
    }


    /**
     * Only really interested in the keyword, so only pick words which start
     * with the same character
     */
    @Override
    public boolean isWordStart(final char c) {
        for (final char firstChar : firstChars) {
            if (firstChar == c) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean isWordPart(final char c) {
        return !Character.isWhitespace(c);
    }


    private char[] firstLettersOf(final String[] strings) {
        final char[] chars = new char[strings.length];
        for (int i = 0; i < strings.length; i++) {
            chars[i] = strings[i].charAt(0);
        }
        return chars;
    }
}
