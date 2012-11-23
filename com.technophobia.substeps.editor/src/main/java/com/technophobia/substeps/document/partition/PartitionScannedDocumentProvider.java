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
package com.technophobia.substeps.document.partition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import com.technophobia.substeps.supplier.Supplier;

/**
 * DocumentProvider that attaches an {@link IDocumentPartitioner} to the
 * document when it is created
 * 
 * @author sforbes
 * 
 */
public class PartitionScannedDocumentProvider extends FileDocumentProvider {

    private final PartitionScannerFactory partitionScannerFactory;

    private IDocument thisDocument = null;

    private final Supplier<PartitionContext> partitionContextSupplier;


    public PartitionScannedDocumentProvider(final PartitionScannerFactory partitionScannerFactory,
            final Supplier<PartitionContext> partitionContextSupplier) {
        this.partitionScannerFactory = partitionScannerFactory;
        this.partitionContextSupplier = partitionContextSupplier;
    }


    @Override
    protected IDocument createDocument(final Object element) throws CoreException {
        thisDocument = super.createDocument(element);
        if (thisDocument != null) {
            attachPartitionerTo(thisDocument, partitionContextSupplier);
        }

        return thisDocument;
    }


    public IDocument getDocuemnt() {
        return thisDocument;
    }


    /**
     * Create a new {@link IDocumentPartitioner} and attach it do document
     * 
     * @param document
     *            The document to attach the partitioner to
     */
    private void attachPartitionerTo(final IDocument document, final Supplier<PartitionContext> partitionContextSupplier) {
        final IDocumentPartitioner partitioner = new FastPartitioner(
                partitionScannerFactory.createScanner(partitionContextSupplier),
                partitionScannerFactory.legalContentTypes());
        partitioner.connect(document);
        document.setDocumentPartitioner(partitioner);
    }
}
