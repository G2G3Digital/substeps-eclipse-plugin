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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

import com.technophobia.eclipse.preference.PluginScopePreferenceLookup;
import com.technophobia.eclipse.preference.PreferenceLookup;
import com.technophobia.eclipse.preference.PreferenceLookupFactory;
import com.technophobia.substeps.editor.preferences.OverridableProjectLocalPreferenceStore;

public class SubstepsProjectPreferenceLookupFactory implements PreferenceLookupFactory<IProject> {

    private final String pluginId;
    private final IPersistentPreferenceStore globalPreferenceStore;


    public SubstepsProjectPreferenceLookupFactory(final String pluginId,
            final IPersistentPreferenceStore globalPreferenceStore) {
        this.pluginId = pluginId;
        this.globalPreferenceStore = globalPreferenceStore;
    }


    @Override
    public PreferenceLookup preferencesFor(final IProject project) {
        final PluginScopePreferenceLookup preferenceLookup = new PluginScopePreferenceLookup(pluginId,
                preferenceStoreFor(project));
        SubstepsPreferences.setDefaults(preferenceLookup);
        return preferenceLookup;
    }


    private IPreferenceStore preferenceStoreFor(final IProject project) {
        return new OverridableProjectLocalPreferenceStore(pluginId, SubstepsPreferences.PROJECT_OVERRIDE.key(), "true",
                project, globalPreferenceStore);
    }

}
