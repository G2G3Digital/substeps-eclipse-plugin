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

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.technophobia.substeps.FeatureEditorPlugin;

public class ProjectLocalPreferenceStore implements IPersistentPreferenceStore {

    private final String pageId;
    private final IProject project;
    private final IPersistentPreferenceStore globalPreferenceStore;

    private final Properties properties;


    public ProjectLocalPreferenceStore(final String pageId, final IProject project,
            final IPersistentPreferenceStore globalPreferenceStore) {
        this.pageId = pageId;
        this.project = project;
        this.globalPreferenceStore = globalPreferenceStore;
        this.properties = loadProjectProperties();
    }


    @Override
    public void addPropertyChangeListener(final IPropertyChangeListener listener) {
        globalPreferenceStore.addPropertyChangeListener(listener);
    }


    @Override
    public boolean contains(final String name) {
        return getLocalPropertyOrNull(name) != null || globalPreferenceStore.contains(name);
    }


    @Override
    public void firePropertyChangeEvent(final String name, final Object oldValue, final Object newValue) {
        globalPreferenceStore.firePropertyChangeEvent(name, oldValue, newValue);
    }


    @Override
    public boolean getBoolean(final String name) {
        final String resourceLocal = getLocalPropertyOrNull(name);
        if (resourceLocal != null) {
            return Boolean.valueOf(resourceLocal).booleanValue();
        }
        return globalPreferenceStore.getBoolean(name);
    }


    @Override
    public boolean getDefaultBoolean(final String name) {
        return globalPreferenceStore.getDefaultBoolean(name);
    }


    @Override
    public double getDefaultDouble(final String name) {
        return globalPreferenceStore.getDefaultDouble(name);
    }


    @Override
    public float getDefaultFloat(final String name) {
        return globalPreferenceStore.getDefaultFloat(name);
    }


    @Override
    public int getDefaultInt(final String name) {
        return globalPreferenceStore.getDefaultInt(name);
    }


    @Override
    public long getDefaultLong(final String name) {
        return globalPreferenceStore.getDefaultLong(name);
    }


    @Override
    public String getDefaultString(final String name) {
        return globalPreferenceStore.getDefaultString(name);
    }


    @Override
    public double getDouble(final String name) {
        final String resourceLocal = getLocalPropertyOrNull(name);
        if (resourceLocal != null) {
            return Double.valueOf(resourceLocal).doubleValue();
        }
        return globalPreferenceStore.getDouble(name);
    }


    @Override
    public float getFloat(final String name) {
        final String resourceLocal = getLocalPropertyOrNull(name);
        if (resourceLocal != null) {
            return Float.valueOf(resourceLocal).floatValue();
        }
        return globalPreferenceStore.getFloat(name);
    }


    @Override
    public int getInt(final String name) {
        final String resourceLocal = getLocalPropertyOrNull(name);
        if (resourceLocal != null) {
            return Integer.valueOf(resourceLocal).intValue();
        }
        return globalPreferenceStore.getInt(name);
    }


    @Override
    public long getLong(final String name) {
        final String resourceLocal = getLocalPropertyOrNull(name);
        if (resourceLocal != null) {
            return Long.valueOf(resourceLocal).longValue();
        }
        return globalPreferenceStore.getLong(name);
    }


    @Override
    public String getString(final String name) {
        final String resourceLocal = getLocalPropertyOrNull(name);
        if (resourceLocal != null) {
            return resourceLocal;
        }
        return globalPreferenceStore.getString(name);
    }


    @Override
    public boolean isDefault(final String name) {
        return getBoolean(name) == getDefaultBoolean(name);
    }


    @Override
    public boolean needsSaving() {
        return globalPreferenceStore.needsSaving();
    }


    @Override
    public void putValue(final String name, final String value) {
        properties.setProperty(name, value);
    }


    @Override
    public void removePropertyChangeListener(final IPropertyChangeListener listener) {
        globalPreferenceStore.removePropertyChangeListener(listener);
    }


    @Override
    public void setDefault(final String name, final double value) {
        globalPreferenceStore.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final float value) {
        globalPreferenceStore.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final int value) {
        globalPreferenceStore.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final long value) {
        globalPreferenceStore.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final String value) {
        globalPreferenceStore.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final boolean value) {
        globalPreferenceStore.setDefault(name, value);
    }


    @Override
    public void setToDefault(final String name) {
        setValue(name, getDefaultString(name));
    }


    @Override
    public void setValue(final String name, final double value) {
        putValue(name, "" + value);
    }


    @Override
    public void setValue(final String name, final float value) {
        putValue(name, "" + value);
    }


    @Override
    public void setValue(final String name, final int value) {
        putValue(name, "" + value);
    }


    @Override
    public void setValue(final String name, final long value) {
        putValue(name, "" + value);
    }


    @Override
    public void setValue(final String name, final String value) {
        putValue(name, value);
    }


    @Override
    public void setValue(final String name, final boolean value) {
        putValue(name, "" + value);
    }


    @Override
    public void save() throws IOException {
        for (final Object keyOb : properties.keySet()) {
            final String key = (String) keyOb;
            final String value = properties.getProperty(key);
            storeProjectProperty(key, value);
        }
    }


    protected String getLocalPropertyOrNull(final String name) {
        return properties.getProperty(name);
    }


    private void storeProjectProperty(final String key, final String value) {
        try {
            project.setPersistentProperty(new QualifiedName(pageId, key), properties.getProperty(key));
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().error(
                    "Could not store project property " + key + " with value " + value + " in project "
                            + project.getName(), ex);
        }
    }


    private Properties loadProjectProperties() {
        final Properties props = new Properties();
        final Map<QualifiedName, String> projectProperties = projectProperties();

        for (final Map.Entry<QualifiedName, String> entry : projectProperties.entrySet()) {
            props.setProperty(entry.getKey().getLocalName(), entry.getValue());
        }
        return props;
    }


    private Map<QualifiedName, String> projectProperties() {
        try {
            return project.getPersistentProperties();
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().warn("Could not get properties for project " + project.getName(), ex);
            return Collections.emptyMap();
        }
    }
}
