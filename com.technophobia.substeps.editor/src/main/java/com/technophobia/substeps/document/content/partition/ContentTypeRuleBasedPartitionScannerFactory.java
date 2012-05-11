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
package com.technophobia.substeps.document.content.partition;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.partition.PartitionScannerFactory;

public class ContentTypeRuleBasedPartitionScannerFactory implements
		PartitionScannerFactory {

	private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	public ContentTypeRuleBasedPartitionScannerFactory(
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
	}

	@Override
	public IPartitionTokenScanner createScanner() {
		return new ContentTypeRuleBasedPartitionScanner(
				contentTypeDefinitionFactory);
	}

	@Override
	public String[] legalContentTypes() {
		return idsFrom(contentTypeDefinitionFactory);
	}

	private String[] idsFrom(
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
		final ContentTypeDefinition[] contentTypeDefinitions = contentTypeDefinitionFactory
				.contentTypeDefinitions();

		final String[] ids = new String[contentTypeDefinitions.length];
		int i = 0;
		for (final ContentTypeDefinition contentTypeDefinition : contentTypeDefinitions) {
			ids[i] = contentTypeDefinition.id();
			i++;
		}

		return ids;
	}
}
