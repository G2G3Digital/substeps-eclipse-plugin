package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

public class DefaultFormattingStrategy implements IFormattingStrategy {

	protected static final String lineSeparator = System
			.getProperty("line.separator");

	public DefaultFormattingStrategy() {
		super();
	}

	@Override
	public void formatterStarts(final String initialIndentation) {
	}

	@Override
	public String format(final String content, final boolean isLineStart,
			final String indentation, final int[] positions) {
		return "";
	}

	@Override
	public void formatterStops() {
	}

}
