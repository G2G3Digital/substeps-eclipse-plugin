package com.technophobia.substeps.document.formatting;

import com.technophobia.substeps.document.content.ContentTypeDefinition;

public interface FormattingContext {

	boolean hasPreviousLine();

	String previousLine() throws InvalidFormatPositionException;

	boolean hasNextLine();

	String nextLine() throws InvalidFormatPositionException;

	boolean hasPreviousContentType();

	ContentTypeDefinition previousContentType()
			throws InvalidFormatPositionException;

	boolean hasNextContentType();

	ContentTypeDefinition nextContentType()
			throws InvalidFormatPositionException;
}
