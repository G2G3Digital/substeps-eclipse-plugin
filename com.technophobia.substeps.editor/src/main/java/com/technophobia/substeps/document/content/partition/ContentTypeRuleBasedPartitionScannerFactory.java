package com.technophobia.substeps.document.content.partition;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.document.partition.PartitionScannerFactory;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Implementation of {@link PartitionScannerFactory} that backs onto a
 * {@link ContentTypeDefinitionFactory}
 * 
 * @author sforbes
 * 
 */
public class ContentTypeRuleBasedPartitionScannerFactory implements PartitionScannerFactory {

    private final ContentTypeDefinitionFactory contentTypeDefinitionFactory;


    public ContentTypeRuleBasedPartitionScannerFactory(final ContentTypeDefinitionFactory contentTypeDefinitionFactory) {
        this.contentTypeDefinitionFactory = contentTypeDefinitionFactory;
    }


    @Override
    public IPartitionTokenScanner createScanner(final Supplier<PartitionContext> partitionContextSupplier) {
        return new ContentTypeRuleBasedPartitionScanner(partitionContextSupplier, contentTypeDefinitionFactory);
    }


    @Override
    public String[] legalContentTypes() {
        final ContentTypeDefinition[] contentTypeDefinitions = contentTypeDefinitionFactory.contentTypeDefinitions();

        final String[] ids = new String[contentTypeDefinitions.length];
        int i = 0;
        for (final ContentTypeDefinition contentTypeDefinition : contentTypeDefinitions) {
            ids[i] = contentTypeDefinition.id();
            i++;
        }

        return ids;
    }
}
