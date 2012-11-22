package com.technophobia.substeps.editor.preferences;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class ResourceLocalPreferenceStore implements IPersistentPreferenceStore {

    private final String pageId;
    private final IResource resource;
    private final IPersistentPreferenceStore globalPreferenceStore;


    public ResourceLocalPreferenceStore(final String pageId, final IResource resource,
            final IPersistentPreferenceStore globalPreferenceStore) {
        this.pageId = pageId;
        this.resource = resource;
        this.globalPreferenceStore = globalPreferenceStore;
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
            return Boolean.getBoolean(resourceLocal);
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
        try {
            resource.setPersistentProperty(new QualifiedName(pageId, name), value);
        } catch (final CoreException e) {
            throw new IllegalStateException("Could not write value " + value + " to property " + name + " on resource "
                    + resource);
        }
    }


    @Override
    public void removePropertyChangeListener(final IPropertyChangeListener listener) {
        globalPreferenceStore.removePropertyChangeListener(listener);
    }


    @Override
    public void setDefault(final String name, final double value) {
        // setValue(name, value);
    }


    @Override
    public void setDefault(final String name, final float value) {

    }


    @Override
    public void setDefault(final String name, final int value) {

    }


    @Override
    public void setDefault(final String name, final long value) {

    }


    @Override
    public void setDefault(final String name, final String defaultObject) {

    }


    @Override
    public void setDefault(final String name, final boolean value) {

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
    }


    private String getLocalPropertyOrNull(final String name) {
        try {
            return resource.getPersistentProperty(new QualifiedName(pageId, name));
        } catch (final CoreException e) {
            return null;
        }
    }
}
