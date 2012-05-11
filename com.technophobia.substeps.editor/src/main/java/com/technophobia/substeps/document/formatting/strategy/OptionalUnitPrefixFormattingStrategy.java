package com.technophobia.substeps.document.formatting.strategy;

import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

public class OptionalUnitPrefixFormattingStrategy extends DefaultFormattingStrategy {

	private final Supplier<FormattingContext> formattingContextSupplier;

	public OptionalUnitPrefixFormattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
		this.formattingContextSupplier = formattingContextSupplier;
	}

	@Override
	public String format(final String content, final boolean isLineStart, final String indentation, final int[] positions) {
		final FormattingContext formattingContext = formattingContextSupplier.get();
		if (formattingContext.hasNextContentType()) {
			return formattingContext.nextContentType().formattingStrategy(formattingContextSupplier).format(content, isLineStart, indentation, positions);
		}
		return "";
	}
}
