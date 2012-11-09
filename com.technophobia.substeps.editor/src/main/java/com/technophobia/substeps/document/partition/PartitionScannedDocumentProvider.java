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
package com.technophobia.substeps.document.partition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.IEditorInput;
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


    public PartitionScannedDocumentProvider(final PartitionScannerFactory partitionScannerFactory) {
        this.partitionScannerFactory = partitionScannerFactory;
    }


    @Override
    protected IDocument createDocument(final Object element) throws CoreException {
        final IDocument document = super.createDocument(element);
        if (document != null) {
            attachPartitionerTo(document, partitionerContextFrom(element));
        }

        return document;
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


    private Supplier<PartitionContext> partitionerContextFrom(final Object element) {
        return new Supplier<PartitionContext>() {

            @Override
            public PartitionContext get() {
                if (element instanceof IEditorInput) {
                    return new EditorInputPartitionContext((IEditorInput) element);
                }
                return new CurrentSelectionPartitionContext();
            }
        };
    }
}
