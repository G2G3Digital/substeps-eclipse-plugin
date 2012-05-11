package com.technophobia.substeps.document.formatting.partition;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TypedPosition;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.InvalidFormatPositionException;

public class PartitionedFormattingContext implements FormattingContext {

	private final IDocument document;
	private final TypedPosition position;
	private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	public PartitionedFormattingContext(final IDocument document, final TypedPosition position, final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		this.document = document;
		this.position = position;
		this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
	}

	@Override
	public boolean hasPreviousLine() {
		return currentLineNumber() > 0;
	}

	@Override
	public String previousLine() throws InvalidFormatPositionException {
		final int currentLineNumber = currentLineNumber();
		if (currentLineNumber <= 0) {
			throw new InvalidFormatPositionException("No line exists before current position");
		}
		return lineAt(currentLineNumber - 1);
	}

	@Override
	public boolean hasNextLine() {
		return currentLineNumber() < numberOfLines() - 1;
	}

	@Override
	public String nextLine() throws InvalidFormatPositionException {
		final int currentLineNumber = currentLineNumber();
		if (currentLineNumber >= numberOfLines() - 1) {
			throw new InvalidFormatPositionException("No line exists after current position");
		}
		return lineAt(currentLineNumber + 1);
	}

	@Override
	public boolean hasPreviousContentType() {
		return position.getOffset() > 0;
	}

	@Override
	public ContentTypeDefinition previousContentType() throws InvalidFormatPositionException {
		if (!hasPreviousContentType()) {
			throw new InvalidFormatPositionException("No content type exists before current position");
		}
		return contentTypeFor(contentTypeAtPosition(position.getOffset() - 1));
	}

	@Override
	public boolean hasNextContentType() {
		return document.getLength() > (position.getOffset() + position.getLength());
	}

	@Override
	public ContentTypeDefinition nextContentType() throws InvalidFormatPositionException {
		if (!hasNextContentType()) {
			throw new InvalidFormatPositionException("No content type exists after current position");
		}
		return contentTypeFor(contentTypeAtPosition(position.getOffset() + position.getLength() + 1));
	}

	private int currentLineNumber() {
		try {
			return document.getLineOfOffset(position.getOffset());
		} catch (final BadLocationException ex) {
			throw new InvalidFormatPositionException("Could not determine current line number", ex);
		}
	}

	private int numberOfLines() {
		return document.getNumberOfLines();
	}

	private String lineAt(final int lineNumber) {
		try {
			final IRegion newRegion = document.getLineInformation(lineNumber);
			return document.get(newRegion.getOffset(), newRegion.getLength());
		} catch (final BadLocationException ex) {
			throw new InvalidFormatPositionException("Could not determine content at line number " + lineNumber, ex);
		}
	}

	private String contentTypeAtPosition(final int position) {
		try {
			return document.getContentType(position);
		} catch (final BadLocationException ex) {
			throw new InvalidFormatPositionException("Could not get content type at position " + position, ex);
		}
	}

	private ContentTypeDefinition contentTypeFor(final String contentType) {
		return contentTypeDefinitionFactory.contentTypeDefintionByName(contentType);
	}
}
