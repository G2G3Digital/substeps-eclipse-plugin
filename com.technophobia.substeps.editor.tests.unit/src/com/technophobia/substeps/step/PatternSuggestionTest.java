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
