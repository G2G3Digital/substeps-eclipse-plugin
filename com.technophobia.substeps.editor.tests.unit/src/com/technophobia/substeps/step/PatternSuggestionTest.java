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
package com.technophobia.substeps.step;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatternSuggestionTest {

    @Test
    public void canMatchStrings() {
        checkMatch("This is a pattern", "This is a pattern", true);
    }


    @Test
    public void matchFailsForNonEqualStrings() {
        checkMatch("This is a pattern", "This is a different pattern", false);
        checkMatch("This is a pattern", "This is a pattern with extra", false);
        checkMatch("This is a pattern", "This is a pat", false);
    }


    @Test
    public void canMatchPatterns() {
        checkMatch("This is a \"([^\"]*)\" pattern", "This is a \"matching\" pattern", true);
    }


    @Test
    public void matchFailsForNonEqualPatterns() {
        checkMatch("This is a \"([^\"]*)\" pattern", "This is an unknown \"matching\" pattern", false);
        checkMatch("This is a \"([^\"]*)\" pattern", "This is a \"matching\" pattern with extra", false);
        checkMatch("This is a \"([^\"]*)\" pattern", "This is a \"matching\" pat", false);
    }


    @Test
    public void canMatchPartialStrings() {
        checkPartialMatch("This is a pattern", "This is a pattern", true);
        checkPartialMatch("This is a pattern", "This is a ", true);
    }


    @Test
    public void partialMatchFailsForNonEqualStrings() {
        checkPartialMatch("This is a pattern", "This is a differen", false);
        checkPartialMatch("This is a pattern", "This is a pattern with extra", false);
    }


    @Test
    public void canMatchPartialPatterns() {
        checkPartialMatch("This is a \"([^\"]*)\" pattern", "This is a \"matching\" pattern", true);
        checkPartialMatch("This is a \"([^\"]*)\" pattern", "This is a \"matching\" p", true);
        checkPartialMatch("This is a \"([^\"]*)\" pattern", "This is a \"matching\"", true);
        checkPartialMatch("This is a \"([^\"]*)\" pattern", "This is a ", true);
    }


    @Test
    public void partialMatchFailsForNonEqualPatterns() {
        checkPartialMatch("This is a \"([^\"]*)\" pattern", "This is an unknown \"matching\" pattern", false);
        checkPartialMatch("This is a \"([^\"]*)\" pattern", "This is a \"matching\" pattern with extra", false);
        checkPartialMatch("This is a \"([^\"]*)\" pattern", "This is a wrong \"matching\"", false);
    }


    private void checkMatch(final String pattern, final String line, final boolean expectedMatch) {
        final PatternSuggestion suggestion = new PatternSuggestion(pattern, pattern);
        assertEquals(suggestion.isMatch(line), expectedMatch);
    }


    private void checkPartialMatch(final String pattern, final String line, final boolean expectedMatch) {
        final PatternSuggestion suggestion = new PatternSuggestion(pattern, pattern);
        assertEquals(expectedMatch, suggestion.isPartialMatch(line));
    }
}
