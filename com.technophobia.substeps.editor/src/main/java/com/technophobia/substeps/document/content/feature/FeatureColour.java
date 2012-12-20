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
package com.technophobia.substeps.document.content.feature;

import org.eclipse.swt.graphics.RGB;

/**
 * Standard colours for Feature files
 * 
 * @author sforbes
 * 
 */
public enum FeatureColour {

    GREEN(new RGB(0, 128, 0)), //
    BLUE(new RGB(0, 0, 128)), //
    LIGHT_BLUE(new RGB(20, 99, 231)), //
    BLACK(new RGB(0, 0, 0)), //
    PINK(new RGB(255, 105, 180)), //
    SLATE(new RGB(112, 138, 144));

    private RGB colour;


    private FeatureColour(final RGB colour) {
        this.colour = colour;
    }


    public RGB colour() {
        return colour;
    }
}
