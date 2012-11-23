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
package com.technophobia.substeps.document.content.feature.partiton;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class SuggestionManagerPredicateRule implements IPredicateRule {

    private final IToken token;
    private final TextExtractor<ICharacterScanner, IToken> textExtractor;


    public SuggestionManagerPredicateRule(final IToken token,
            final TextExtractor<ICharacterScanner, IToken> textExtractor) {
        this.token = token;
        this.textExtractor = textExtractor;
    }


    @Override
    public IToken getSuccessToken() {
        return token;
    }


    @Override
    public IToken evaluate(final ICharacterScanner scanner) {
        return evaluate(scanner, false);
    }


    @Override
    public IToken evaluate(final ICharacterScanner scanner, final boolean resume) {
        final IToken resToken = textExtractor.extractText(scanner);

        return resToken != null ? resToken : Token.UNDEFINED;
    }
}
