/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.formatting.partition;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TypedPosition;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.InvalidFormatPositionException;

public class PartitionedFormattingContext implements FormattingContext {

	private final TypedPosition[] positions;
	private final int currentPosition;
	private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	public PartitionedFormattingContext(final TypedPosition[] positions,
			final int currentPosition,
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		this.positions = positions;
		this.currentPosition = currentPosition;
		this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
	}

	@Override
	public boolean hasPreviousContentType() {
		return locatePreviousContentTypeOrNull() != null;
	}

	@Override
	public ContentTypeDefinition previousContentType()
			throws InvalidFormatPositionException {
		final ContentTypeDefinition prevContentType = locatePreviousContentTypeOrNull();
		if (prevContentType == null) {
			throw new InvalidFormatPositionException(
					"No content type exists before current position");
		}
		return prevContentType;
	}

	@Override
	public boolean hasNextContentType() {
		return locateNextContentTypeOrNull() != null;
	}

	@Override
	public ContentTypeDefinition nextContentType()
			throws InvalidFormatPositionException {
		final ContentTypeDefinition nextContentType = locateNextContentTypeOrNull();
		if (nextContentType == null) {
			throw new InvalidFormatPositionException(
					"No content type exists after current position");
		}
		return nextContentType;
	}

	private ContentTypeDefinition contentTypeFor(final String contentType) {
		return contentTypeDefinitionFactory
				.contentTypeDefintionByName(contentType);
	}

	private ContentTypeDefinition locatePreviousContentTypeOrNull() {
		int pos = currentPosition - 1;
		while (pos >= 0) {
			final String type = positions[pos].getType();
			if (!IDocument.DEFAULT_CONTENT_TYPE.equals(type)) {
				return contentTypeFor(type);
			}
			pos--;
		}
		return null;
	}

	private ContentTypeDefinition locateNextContentTypeOrNull() {
		int pos = currentPosition + 1;
		while (pos < positions.length) {
			final String type = positions[pos].getType();
			if (!IDocument.DEFAULT_CONTENT_TYPE.equals(type)) {
				return contentTypeFor(type);
			}
			pos++;
		}
		return null;
	}
}
