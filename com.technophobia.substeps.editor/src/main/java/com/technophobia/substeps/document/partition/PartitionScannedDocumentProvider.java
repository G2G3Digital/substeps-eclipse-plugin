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
