package com.technophobia.substeps.runner;

import java.util.HashMap;

public class TestIdMap {
    private final HashMap<ITestIdentifier, String> idMap = new HashMap<ITestIdentifier, String>();

    private int nextId = 1;


    public String getTestId(final ITestIdentifier identifier) {
        final Object id = idMap.get(identifier);
        if (id != null)
            return (String) id;
        final String newId = Integer.toString(nextId++);
        idMap.put(identifier, newId);
        return newId;
    }


    public String getTestId(final ITestReference ref) {
        return getTestId(ref.getIdentifier());
    }
}
