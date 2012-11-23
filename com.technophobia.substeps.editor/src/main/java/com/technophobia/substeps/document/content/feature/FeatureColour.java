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
