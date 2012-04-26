package com.technophobia.substeps.document.formatting;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.formatter.ContentFormatter;

import com.technophobia.substeps.supplier.Supplier;

public class ContextAwareContentFormatter extends ContentFormatter implements
		Supplier<FormattingContext> {

	private FormattingContext currentContext = null;
	private final FormattingContextFactory formattingContextFactory;

	public ContextAwareContentFormatter(
			final FormattingContextFactory formattingContextFactory) {
		super();
		this.formattingContextFactory = formattingContextFactory;
	}

	@Override
	public void format(final IDocument document, final IRegion region) {
		updateCurrentContext(document, region);
		super.format(document, region);
	}

	@Override
	public FormattingContext get() {
		return currentContext;
	}

	private void updateCurrentContext(final IDocument document,
			final IRegion region) {
		currentContext = formattingContextFactory.createFor(document, region);
	}
}
