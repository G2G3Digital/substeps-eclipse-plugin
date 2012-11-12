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

/**
 * Implementation of {@link FormattingContext} that is
 * {@link ContentTypeDefinition} aware
 * 
 * @author sforbes
 * 
 */
public class PartitionedFormattingContext implements FormattingContext {

    private final TypedPosition[] positions;
    private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;
    private final int currentPosition;


    public PartitionedFormattingContext(final TypedPosition[] positions, final int currentPosition,
            final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
        this.positions = positions;
        this.currentPosition = currentPosition;
        this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
    }


    @Override
    public ContentTypeDefinition currentContentType() {
        return contentTypeDefinitionFactory.contentTypeDefintionByName(positions[currentPosition].getType());
    }


    @Override
    public boolean hasPreviousContent() {
        return locatePreviousContentTypeOrNull() != null;
    }


    @Override
    public ContentTypeDefinition inspectPreviousContentType() throws InvalidFormatPositionException {
        final PositionalContentTypeDefinitionHolder prevContentType = locatePreviousContentTypeOrNull();
        if (prevContentType == null) {
            throw new InvalidFormatPositionException("No content type exists before current position");
        }
        return contentTypeDefinitionFactory.contentTypeDefintionByName(positions[prevContentType.getPosition()]
                .getType());
    }


    @Override
    public boolean hasMoreContent() {
        return locateNextContentTypeOrNull() != null;
    }


    @Override
    public FormattingContext impersonateNextContentContext() throws InvalidFormatPositionException {
        final PositionalContentTypeDefinitionHolder nextContentType = locateNextContentTypeOrNull();
        if (nextContentType == null) {
            throw new InvalidFormatPositionException("No content type exists after current position");
        }
        return new SkipAheadFormattingContext(positions, currentPosition, nextContentType.getPosition(),
                contentTypeDefinitionFactory);
    }


    private PositionalContentTypeDefinitionHolder locatePreviousContentTypeOrNull() {
        int pos = previousPosition();
        while (pos >= 0) {
            final String type = positions[pos].getType();
            if (!IDocument.DEFAULT_CONTENT_TYPE.equals(type)) {
                return new PositionalContentTypeDefinitionHolder(pos);
            }
            pos--;
        }
        return null;
    }


    private PositionalContentTypeDefinitionHolder locateNextContentTypeOrNull() {
        int pos = nextPosition();
        while (pos < positions.length) {
            final String type = positions[pos].getType();
            if (!IDocument.DEFAULT_CONTENT_TYPE.equals(type)) {
                return new PositionalContentTypeDefinitionHolder(pos);
            }
            pos++;
        }
        return null;
    }


    protected int previousPosition() {
        return currentPosition - 1;
    }


    protected int nextPosition() {
        return currentPosition + 1;
    }

    private static final class PositionalContentTypeDefinitionHolder {
        private final int position;


        public PositionalContentTypeDefinitionHolder(final int position) {
            this.position = position;
        }


        public int getPosition() {
            return position;
        }
    }
}
