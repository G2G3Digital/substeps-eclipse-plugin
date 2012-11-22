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
