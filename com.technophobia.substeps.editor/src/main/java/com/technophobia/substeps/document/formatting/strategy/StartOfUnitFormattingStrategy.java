package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.formatter.IFormattingStrategy;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

public class StartOfUnitFormattingStrategy extends DefaultFormattingStrategy {

	private final IFormattingStrategy formattingStrategy;
	private final Supplier<FormattingContext> formattingContextSupplier;

	public StartOfUnitFormattingStrategy(
			final Supplier<FormattingContext> formattingContextSupplier,
			final IFormattingStrategy formattingStrategy) {
		this.formattingContextSupplier = formattingContextSupplier;
		this.formattingStrategy = formattingStrategy;

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
