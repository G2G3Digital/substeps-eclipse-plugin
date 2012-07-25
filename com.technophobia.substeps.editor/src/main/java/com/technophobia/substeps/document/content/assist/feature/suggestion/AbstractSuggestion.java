package com.technophobia.substeps.document.content.assist.feature.suggestion;

public abstract class AbstractSuggestion implements Suggestion {

    private final String line;
    private final String type;


    public AbstractSuggestion(final String line, final String type) {
        super();
        this.line = line;
        this.type = type;
    }


    @Override
    public String getSuggestionString() {
        return line + " - " + type;
    }


    @Override
    public int compareTo(final Suggestion o) {
        return getSuggestionString().compareTo(o.getSuggestionString());
    }
}
