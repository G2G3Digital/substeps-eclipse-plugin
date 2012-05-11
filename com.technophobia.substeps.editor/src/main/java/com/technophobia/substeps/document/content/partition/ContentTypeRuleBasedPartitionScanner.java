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

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;

public class ContentTypeRuleBasedPartitionScanner extends
		RuleBasedPartitionScanner {

	private final String[] contentTypes;

	public ContentTypeRuleBasedPartitionScanner(
			final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {

		final ContentTypeDefinition[] contentTypeDefinitions = contentTypeDefinitionFactory
				.contentTypeDefinitions();
		final String[] contentTypeDefinitionIds = new String[contentTypeDefinitions.length];
		final IPredicateRule[] partitionRules = new IPredicateRule[contentTypeDefinitions.length];
		int i = 0;
		for (final ContentTypeDefinition contentTypeDefinition : contentTypeDefinitions) {
			contentTypeDefinitionIds[i] = contentTypeDefinition.id();
			partitionRules[i] = contentTypeDefinition.partitionRule();
			i++;
		}

		this.contentTypes = contentTypeDefinitionIds;
		setPredicateRules(partitionRules);
	}

	protected String[] contentTypes() {
		return contentTypes;
	}
}
