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
package com.technophobia.substeps.document.formatting.strategy;

public abstract class IndentationFormattingStrategy extends DefaultFormattingStrategy {

	@Override
	public void formatterStarts(final String initialIndentation) {
		super.formatterStarts(initialIndentation);
	}

	@Override
	public String format(final String content, final boolean isLineStart, final String indentation, final int[] positions) {
		final boolean hasLineBreak = content.endsWith(lineSeparator);

		String indentedContent = isLineStart ? indent() + content.trim() : content.trim();	
		if (hasLineBreak) {
			indentedContent = indentedContent + lineSeparator;
		}
		return indentedContent;
	}

	protected abstract String indent();
}
