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
package com.technophobia.substeps.colour;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Cache of all {@link Colour}s used so far, keyed on their {@link RBG}
 * 
 * @author sforbes
 * 
 */
public class ColourManager {

    protected Map<RGB, Color> colourTable = new HashMap<RGB, Color>(10);


    /**
     * Get colour identified by rgb, creating if it doesn't yet exist
     * 
     * @param rgb
     *            The rgb value
     * @return Colour
     */
    public Color getColor(final RGB rgb) {
        Color colour = colourTable.get(rgb);
        if (colour == null) {
            colour = new Color(Display.getCurrent(), rgb);
            colourTable.put(rgb, colour);
        }
        return colour;
    }


    /**
     * Dispose all current colours
     */
    public void dispose() {
        final Iterator<Color> e = colourTable.values().iterator();
        while (e.hasNext())
            e.next().dispose();
    }
}
