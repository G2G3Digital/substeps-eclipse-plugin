package com.technophobia.eclipse.lookup;

public interface PreferenceLookup {

    String valueFor(String key);


    boolean booleanFor(String key);


    void setDefault(String key, String value);


    void setDefault(String key, boolean value);
}
