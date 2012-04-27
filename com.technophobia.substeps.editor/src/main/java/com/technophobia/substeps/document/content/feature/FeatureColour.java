package com.technophobia.substeps.document.content.feature;

import org.eclipse.swt.graphics.RGB;

public enum FeatureColour {

	GREEN(new RGB(0, 128, 0)), //
	BLUE(new RGB(0, 0, 128)), //
	BLACK(new RGB(0, 0, 0)), //
	PINK(new RGB(255, 105, 180));

	private RGB colour;

	private FeatureColour(final RGB colour) {
		this.colour = colour;
	}

	public RGB colour() {
		return colour;
	}
}
