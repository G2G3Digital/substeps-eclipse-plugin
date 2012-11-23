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
