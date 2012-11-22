package com.technophobia.substeps.editor.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPersistentPreferenceStore;

public class OverridableProjectLocalPreferenceStore extends ProjectLocalPreferenceStore {

    private final String controlPropertyName;
    private final String overrideGlobalOnValue;


    public OverridableProjectLocalPreferenceStore(final String pageId, final String controlPropertyName,
            final String overrideGlobalOnValue, final IProject project,
            final IPersistentPreferenceStore globalPreferenceStore) {
        super(pageId, project, globalPreferenceStore);
        this.controlPropertyName = controlPropertyName;
        this.overrideGlobalOnValue = overrideGlobalOnValue;

    }


    @Override
    protected String getLocalPropertyOrNull(final String name) {
        final String controlValue = super.getLocalPropertyOrNull(controlPropertyName);
        if (overrideGlobalOnValue.equals(controlValue)) {
            return super.getLocalPropertyOrNull(name);
        }
        return null;
    }
}
