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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class SuggestionManagerPredicateRuleTest {

    private static final IToken token = new Token("Test");

    private Mockery context;

    private TextExtractor<ICharacterScanner, IToken> textExtractor;

    private ICharacterScanner characterScanner;

    private IPredicateRule predicateRule;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.textExtractor = context.mock(TextExtractor.class);

        this.characterScanner = context.mock(ICharacterScanner.class);

        this.predicateRule = new SuggestionManagerPredicateRule(token, textExtractor);
    }


    @Test
    public void validSuggestionReturnsToken() {

        context.checking(new Expectations() {
            {
                oneOf(textExtractor).extractText(characterScanner);
                will(returnValue(token));
            }
        });

        assertThat(predicateRule.evaluate(characterScanner), is(token));
    }


    @Test
    public void unrecognisedSuggestionReturnsUndefinedToken() {

        context.checking(new Expectations() {
            {
                oneOf(textExtractor).extractText(characterScanner);
                will(returnValue(null));
            }
        });

        assertThat(predicateRule.evaluate(characterScanner), is(Token.UNDEFINED));
    }
}
