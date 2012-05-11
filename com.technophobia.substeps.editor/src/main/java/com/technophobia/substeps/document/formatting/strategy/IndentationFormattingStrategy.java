package com.technophobia.substeps.document.formatting.strategy;

public abstract class IndentationFormattingStrategy extends DefaultFormattingStrategy {

	@Override
	public void formatterStarts(final String initialIndentation) {
		super.formatterStarts(initialIndentation);
	}

	@Override
	public String format(final String content, final boolean isLineStart, final String indentation, final int[] positions) {
		final boolean hasLineBreak = content.endsWith(lineSeparator);

		String indentedContent = indent() + content.trim();
		if (hasLineBreak) {
			indentedContent = indentedContent + lineSeparator;
		}
		return indentedContent;
	}

	protected abstract String indent();
}
