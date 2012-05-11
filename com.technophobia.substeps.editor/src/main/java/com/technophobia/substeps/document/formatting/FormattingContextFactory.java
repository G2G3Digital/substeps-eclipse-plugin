package com.technophobia.substeps.document.formatting;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TypedPosition;

public interface FormattingContextFactory {

	FormattingContext createFor(IDocument document, TypedPosition position);
}
