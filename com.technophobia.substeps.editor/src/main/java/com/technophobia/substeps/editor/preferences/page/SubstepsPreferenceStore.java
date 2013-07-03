package com.technophobia.substeps.editor.preferences.page;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.technophobia.substeps.nature.SubstepsNature;
import com.technophobia.substeps.preferences.SubstepsPreferences;

public class SubstepsPreferenceStore implements IPreferenceStore, IPersistentPreferenceStore {

    private static final String PROJECT_NATURE_KEY = SubstepsPreferences.PROJECT_NATURE.key();
    private final IPreferenceStore delegate;
    private final IProject project;


    public SubstepsPreferenceStore(final IPreferenceStore delegate, final IProject project) {
        this.delegate = delegate;
        this.project = project;
    }


    @Override
    public void save() throws IOException {
        final boolean hasNature = getBoolean(PROJECT_NATURE_KEY);
        if (hasNature) {
            SubstepsNature.ensureProjectHasNature(project);
        } else {
            SubstepsNature.ensureProjectDoesNotHaveNature(project);
        }
        ((IPersistentPreferenceStore) delegate).save();
    }


    @Override
    public void addPropertyChangeListener(final IPropertyChangeListener listener) {
        delegate.addPropertyChangeListener(listener);
    }


    @Override
    public boolean contains(final String name) {
        return delegate.contains(name) || PROJECT_NATURE_KEY.equals(name);
    }


    @Override
    public void firePropertyChangeEvent(final String name, final Object oldValue, final Object newValue) {
        delegate.firePropertyChangeEvent(name, oldValue, newValue);
    }


    @Override
    public boolean getBoolean(final String name) {
        return delegate.getBoolean(name);
    }


    @Override
    public boolean getDefaultBoolean(final String name) {
        if (PROJECT_NATURE_KEY.equals(name)) {
            return SubstepsNature.isSubstepsProject(project);
        }
        return delegate.getBoolean(name);
    }


    @Override
    public double getDefaultDouble(final String name) {
        return delegate.getDefaultDouble(name);
    }


    @Override
    public float getDefaultFloat(final String name) {
        return delegate.getDefaultFloat(name);
    }


    @Override
    public int getDefaultInt(final String name) {
        return delegate.getDefaultInt(name);
    }


    @Override
    public long getDefaultLong(final String name) {
        return delegate.getDefaultLong(name);
    }


    @Override
    public String getDefaultString(final String name) {
        return delegate.getDefaultString(name);
    }


    @Override
    public double getDouble(final String name) {
        return delegate.getDefaultDouble(name);
    }


    @Override
    public float getFloat(final String name) {
        return delegate.getFloat(name);
    }


    @Override
    public int getInt(final String name) {
        return delegate.getInt(name);
    }


    @Override
    public long getLong(final String name) {
        return delegate.getLong(name);
    }


    @Override
    public String getString(final String name) {
        return delegate.getString(name);
    }


    @Override
    public boolean isDefault(final String name) {
        return delegate.isDefault(name);
    }


    @Override
    public boolean needsSaving() {
        return delegate.needsSaving();
    }


    @Override
    public void putValue(final String name, final String value) {
        delegate.putValue(name, value);
    }


    @Override
    public void removePropertyChangeListener(final IPropertyChangeListener listener) {
        delegate.removePropertyChangeListener(listener);
    }


    @Override
    public void setDefault(final String name, final double value) {
        delegate.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final float value) {
        delegate.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final int value) {
        delegate.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final long value) {
        delegate.setDefault(name, value);
    }


    @Override
    public void setDefault(final String name, final String defaultObject) {
        delegate.setDefault(name, defaultObject);
    }


    @Override
    public void setDefault(final String name, final boolean value) {
        delegate.setDefault(name, value);
    }


    @Override
    public void setToDefault(final String name) {
        delegate.setToDefault(name);
    }


    @Override
    public void setValue(final String name, final double value) {
        delegate.setValue(name, value);
    }


    @Override
    public void setValue(final String name, final float value) {
        delegate.setValue(name, value);
    }


    @Override
    public void setValue(final String name, final int value) {
        delegate.setValue(name, value);
    }


    @Override
    public void setValue(final String name, final long value) {
        delegate.setValue(name, value);
    }


    @Override
    public void setValue(final String name, final String value) {
        delegate.setValue(name, value);
    }


    @Override
    public void setValue(final String name, final boolean value) {
        delegate.setValue(name, value);
    }
}
