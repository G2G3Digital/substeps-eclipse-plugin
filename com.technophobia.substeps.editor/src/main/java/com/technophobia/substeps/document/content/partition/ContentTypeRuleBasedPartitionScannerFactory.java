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
