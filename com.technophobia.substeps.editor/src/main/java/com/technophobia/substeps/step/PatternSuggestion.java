package com.technophobia.substeps.step;

import java.util.regex.Pattern;

public class PatternSuggestion extends Suggestion {

    private final Pattern pattern;


    public PatternSuggestion(final String pattern, final String text) {
        super(text);
        this.pattern = Pattern.compile(pattern);
    }


    @Override
    public boolean isMatch(final String line) {
        return pattern.matcher(line).matches();
    }


    @Override
    public boolean isPartialMatch(final String line) {
        return pattern.matcher(line).hitEnd();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PatternSuggestion other = (PatternSuggestion) obj;
        if (pattern == null) {
            if (other.pattern != null)
                return false;
        } else if (!pattern.equals(other.pattern))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "PatternSuggestion: " + getText() + ", with pattern " + pattern;
    }
}
