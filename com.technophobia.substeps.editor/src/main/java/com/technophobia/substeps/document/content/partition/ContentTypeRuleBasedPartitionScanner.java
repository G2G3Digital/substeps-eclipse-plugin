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
