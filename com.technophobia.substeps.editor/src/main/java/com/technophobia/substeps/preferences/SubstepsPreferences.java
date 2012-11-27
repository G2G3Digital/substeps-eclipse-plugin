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
package com.technophobia.substeps.preferences;

import com.technophobia.eclipse.preference.PreferenceLookup;

public enum SubstepsPreferences {

    ENABLE_PROBLEMS("enable.problems") {
        @Override
        protected void setDefault(final PreferenceLookup lookup) {
            lookup.setDefault(key(), true);
        }
    },
    PROJECT_OVERRIDE("project.override") {
        @Override
        protected void setDefault(final PreferenceLookup lookup) {
            lookup.setDefault(key(), false);
        }
    },
    FEATURE_FOLDER("folder.feature") {
        @Override
        protected void setDefault(final PreferenceLookup lookup) {
            lookup.setDefault(key(), "");
        }
    },
    SUBSTEPS_FOLDER("folder.substeps") {
        @Override
        protected void setDefault(final PreferenceLookup lookup) {
            lookup.setDefault(key(), "");
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
