package com.technophobia.eclipse.preference;

public interface PreferenceLookupFactory<Context> {

    PreferenceLookup preferencesFor(Context context);
}
