package com.technophobia.substeps.step;

public enum SuggestionType {

    FEATURE(SuggestionSource.SUBSTEP_DEFINITION, SuggestionSource.PROJECT_STEP_IMPLEMENTATION,
            SuggestionSource.EXTERNAL_STEP_IMPLEMENTATION), //
    SUBSTEP(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, SuggestionSource.EXTERNAL_STEP_IMPLEMENTATION);

    private final SuggestionSource[] suggestionSources;


    private SuggestionType(final SuggestionSource... suggestionSources) {
        this.suggestionSources = suggestionSources;
    }


    public boolean isPermittedSuggestionSource(final SuggestionSource suggestionSource) {
        for (final SuggestionSource source : suggestionSources) {
            if (source.equals(suggestionSource)) {
                return true;
            }
        }
        return false;
    }
}
