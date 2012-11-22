package com.technophobia.eclipse.preference;

public interface PreferenceLookup {

    String valueFor(String key);


    boolean booleanFor(String key);


    void setDefault(String key, String value);


    void setDefault(String key, boolean value);
}
