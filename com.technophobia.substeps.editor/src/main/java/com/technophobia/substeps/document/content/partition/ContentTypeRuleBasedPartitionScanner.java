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
