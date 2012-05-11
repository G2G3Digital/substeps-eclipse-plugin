package com.technophobia.substeps.document.formatting.strategy;

public class FixedIndentFormattingStrategy extends IndentationFormattingStrategy {

	private final String indent;

	public FixedIndentFormattingStrategy(final String indent) {
		this.indent = indent;
	}

	@Override
	protected String indent() {
		return indent;
	}

}
