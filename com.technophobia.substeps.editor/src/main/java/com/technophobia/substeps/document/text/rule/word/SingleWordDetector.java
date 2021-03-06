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
