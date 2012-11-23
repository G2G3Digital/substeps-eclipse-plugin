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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternSuggestion extends Suggestion {

    private final Pattern pattern;


    public PatternSuggestion(final String pattern, final String text) {
        super(text);
        this.pattern = Pattern.compile(pattern);
    }


    @Override
    public boolean isMatch(final String line) {
        final Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }


    @Override
    public boolean isPartialMatch(final String line) {
        final Matcher matcher = pattern.matcher(line);
        return matcher.matches() || matcher.hitEnd();
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
