/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    BLACK(new RGB(0, 0, 0)), //
    PINK(new RGB(255, 105, 180)),
    SLATE(new RGB(112, 138, 144));
    
    private RGB colour;


    private FeatureColour(final RGB colour) {
        this.colour = colour;
    }


    public RGB colour() {
        return colour;
    }
}
