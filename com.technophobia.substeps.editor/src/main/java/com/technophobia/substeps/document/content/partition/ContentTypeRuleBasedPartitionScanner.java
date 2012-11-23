/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.document.content.partition;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Partition Scanner that partitions editor elements based on their Content type
 * 
 * @author sforbes
 * 
 */
public class ContentTypeRuleBasedPartitionScanner extends RuleBasedPartitionScanner {

    private final String[] contentTypes;


    public ContentTypeRuleBasedPartitionScanner(final Supplier<PartitionContext> partitionContextSupplier,
            final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {

        final ContentTypeDefinition[] contentTypeDefinitions = contentTypeDefinitionFactory.contentTypeDefinitions();
        final String[] contentTypeDefinitionIds = new String[contentTypeDefinitions.length];
        final IPredicateRule[] partitionRules = new IPredicateRule[contentTypeDefinitions.length + 2];

        final Token key = new Token(IDocument.DEFAULT_CONTENT_TYPE);

        partitionRules[0] = new LeadingWhitespacePredicateRule(key, "\t");
        partitionRules[1] = new LeadingWhitespacePredicateRule(key, " ");
        int i = 0;
        for (final ContentTypeDefinition contentTypeDefinition : contentTypeDefinitions) {
            contentTypeDefinitionIds[i] = contentTypeDefinition.id();
            partitionRules[i + 2] = contentTypeDefinition.partitionRule(partitionContextSupplier);
            i++;
        }

        this.contentTypes = contentTypeDefinitionIds;
        setPredicateRules(partitionRules);
    }


    protected String[] contentTypes() {
        return contentTypes;
    }
}
