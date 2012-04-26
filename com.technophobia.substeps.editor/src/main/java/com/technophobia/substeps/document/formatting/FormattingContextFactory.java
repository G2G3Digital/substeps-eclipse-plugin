package com.technophobia.substeps.document.formatting;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public interface FormattingContextFactory {

	FormattingContext createFor(IDocument document, IRegion region);
}
