package com.technophobia.substeps.document.formatting;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;

public class PartitionedFormattingContext implements FormattingContext {

	private final IDocument document;
	private final IRegion region;
	private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	public PartitionedFormattingContext(final IDocument document,
			final IRegion region,
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		this.document = document;
		this.region = region;
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
			throw new InvalidFormatPositionException(
					"No line exists before current position");
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
			throw new InvalidFormatPositionException(
					"No line exists after current position");
		}
		return lineAt(currentLineNumber + 1);
	}

	@Override
	public boolean hasPreviousContentType() {
		return region.getOffset() > 0;
	}

	@Override
	public ContentTypeDefinition previousContentType()
			throws InvalidFormatPositionException {
		if (!hasPreviousContentType()) {
			throw new InvalidFormatPositionException(
					"No content type exists before current position");
		}
		return contentTypeFor(contentTypeAtPosition(region.getOffset() - 1));
	}

	@Override
	public boolean hasNextContentType() {
		return document.getLength() > (region.getOffset() + region.getLength());
	}

	@Override
	public ContentTypeDefinition nextContentType()
			throws InvalidFormatPositionException {
		if (!hasNextContentType()) {
			throw new InvalidFormatPositionException(
					"No content type exists after current position");
		}
		return contentTypeFor(contentTypeAtPosition(region.getOffset()
				+ region.getLength() + 1));
	}

	private int currentLineNumber() {
		try {
			return document.getLineOfOffset(region.getOffset());
		} catch (final BadLocationException ex) {
			throw new InvalidFormatPositionException(
					"Could not determine current line number", ex);
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
			throw new InvalidFormatPositionException(
					"Could not determine content at line number " + lineNumber,
					ex);
		}
	}

	private String contentTypeAtPosition(final int position) {
		try {
			return document.getContentType(position);
		} catch (final BadLocationException ex) {
			throw new InvalidFormatPositionException(
					"Could not get content type at position " + position, ex);
		}
	}

	private ContentTypeDefinition contentTypeFor(final String contentType) {
		return contentTypeDefinitionFactory
				.contentTypeDefintionByName(contentType);
	}
}
