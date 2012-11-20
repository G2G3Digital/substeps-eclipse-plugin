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
