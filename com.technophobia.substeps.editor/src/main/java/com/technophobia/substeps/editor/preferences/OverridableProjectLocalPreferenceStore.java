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
package com.technophobia.substeps.editor.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPersistentPreferenceStore;

public class OverridableProjectLocalPreferenceStore extends ProjectLocalPreferenceStore {

    private final String controlPropertyName;
    private final String overrideGlobalOnValue;
    private final List<String> excludedPropertyNames;


    public OverridableProjectLocalPreferenceStore(final String pageId, final String controlPropertyName,
            final String overrideGlobalOnValue, final IProject project,
            final IPersistentPreferenceStore globalPreferenceStore, final String... excludedPropertyNames) {
        super(pageId, project, globalPreferenceStore);
        this.controlPropertyName = controlPropertyName;
        this.overrideGlobalOnValue = overrideGlobalOnValue;
        this.excludedPropertyNames = Arrays.asList(excludedPropertyNames);

    }


    @Override
    protected String getLocalPropertyOrNull(final String name) {
        if (isExcluded(name) || isControlAllowed()) {
            return super.getLocalPropertyOrNull(name);
        }
        return null;
    }


    private boolean isExcluded(final String name) {
        return excludedPropertyNames.contains(name);
    }


    private boolean isControlAllowed() {
        final String controlValue = super.getLocalPropertyOrNull(controlPropertyName);
        return overrideGlobalOnValue.equals(controlValue);
    }
}
