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

public class Suggestion implements Comparable<Suggestion> {

    private final String text;


    public Suggestion(final String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }


    public boolean isMatch(final String line) {
        return text.equals(line);
    }


    public boolean isPartialMatch(final String line) {
        return text.toUpperCase().startsWith(line.toUpperCase());
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Suggestion other = (Suggestion) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }


    @Override
    public int compareTo(final Suggestion other) {
        return other != null ? text.compareTo(other.text) : -1;
    }


    @Override
    public String toString() {
        return "Suggestion: " + text;
    }
}
