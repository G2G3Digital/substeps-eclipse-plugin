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
package com.technophobia.substeps.document.text.rule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class MultiLineFragmentedSequenceRuleTest {

    private static final String NEWLINE = System.getProperty("line.separator");

    private Mockery context;

    private IToken token;
    private ICharacterScanner characterScanner;

    private MultiLineFragmentedSequenceRule rule;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.token = context.mock(IToken.class);
        this.characterScanner = context.mock(ICharacterScanner.class);

        this.rule = new MultiLineFragmentedSequenceRule("Start", "End", token);
    }


    @Test
    public void findsSequenceWhenSequenceIsNonFragmented() {
        prepareCharacterScanner("End");

        assertTrue(rule.sequenceDetected(characterScanner, " End".toCharArray(), false));
    }


    @Test
    public void findsSequenceWhenSequenceIsFragmented() {
        prepareCharacterScanner("E  n\t" + NEWLINE + "d");

        assertTrue(rule.sequenceDetected(characterScanner, " End".toCharArray(), false));
    }


    @Test
    public void failsWhenSequenceNotDetected() {
        prepareCharacterScanner("S");

        context.checking(new Expectations() {
            {
                oneOf(characterScanner).unread();
            }
        });

        assertFalse(rule.sequenceDetected(characterScanner, " End".toCharArray(), false));
    }


    private void prepareCharacterScanner(final String text) {
        context.checking(new Expectations() {
            {
                for (final char c : text.toCharArray()) {
                    oneOf(characterScanner).read();
                    will(returnValue((int) c));
                }
            }
        });
    }
}
