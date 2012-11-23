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
package com.technophobia.substeps;

import org.eclipse.jface.preference.IPreferenceStore;

import com.technophobia.eclipse.lookup.PreferenceLookup;

public class PluginScopePreferenceLookup implements PreferenceLookup {

    private final String pluginId;
    private final IPreferenceStore preferenceStore;


    public PluginScopePreferenceLookup(final String pluginId, final IPreferenceStore preferenceStore) {
        this.pluginId = pluginId;
        this.preferenceStore = preferenceStore;
    }


    @Override
    public String valueFor(final String key) {
        return preferenceStore.getString(key);
    }


    @Override
    public boolean booleanFor(final String key) {
        return preferenceStore.getBoolean(key);
    }


    @Override
    public void setDefault(final String key, final String value) {

        preferenceStore.setDefault(key, value);
    }


    @Override
    public void setDefault(final String key, final boolean value) {

        preferenceStore.setDefault(key, value);
    }
}
