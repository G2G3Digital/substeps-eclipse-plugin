package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.core.runtime.Status;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

public class OptionalUnitPrefixFormattingStrategy extends
		DefaultFormattingStrategy {

	private final Supplier<FormattingContext> formattingContextSupplier;

	public OptionalUnitPrefixFormattingStrategy(
			final Supplier<FormattingContext> formattingContextSupplier) {
		this.formattingContextSupplier = formattingContextSupplier;
	}

	@Override
	public String format(final String content, final boolean isLineStart,
			final String indentation, final int[] positions) {
		FeatureEditorPlugin.log(Status.INFO, "Formatting line: " + content
				+ ", isLineStart: " + isLineStart + ", indentation: "
				+ indentation);
		return super.format(content, isLineStart, indentation, positions);
	}
}
