package com.technophobia.substeps.preferences;

import com.technophobia.eclipse.lookup.PreferenceLookup;

public enum SubstepsPreferences {

    ENABLE_PROBLEMS("enable.problems") {
        @Override
        protected void setDefault(final PreferenceLookup lookup) {
            lookup.setDefault(key(), true);
        }
    };

    private final String key;


    private SubstepsPreferences(final String key) {
        this.key = key;
    }


    public String key() {
        return key;
    }


    protected abstract void setDefault(PreferenceLookup lookup);


    public static void setDefaults(final PreferenceLookup lookup) {
        for (final SubstepsPreferences preference : values()) {
            preference.setDefault(lookup);
        }
    }
}
