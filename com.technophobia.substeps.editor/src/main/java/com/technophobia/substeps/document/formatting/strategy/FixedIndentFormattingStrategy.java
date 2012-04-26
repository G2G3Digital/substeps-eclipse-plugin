package com.technophobia.substeps.document.formatting.strategy;

public class FixedIndentFormattingStrategy extends DefaultFormattingStrategy {

	private final String indent;

	public FixedIndentFormattingStrategy(final String indent) {
		this.indent = indent;
	}

	@Override
	public void formatterStarts(final String initialIndentation) {
		super.formatterStarts(initialIndentation);
	}

	@Override
	public String format(final String content, final boolean isLineStart,
			final String indentation, final int[] positions) {
		final boolean hasLineBreak = content.endsWith(lineSeparator);

		String indentedContent = indent + content.trim();
		if (hasLineBreak) {
			indentedContent = indentedContent + lineSeparator;
		}
		return indentedContent;
	}
}
