package com.technophobia.substeps.document.partition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class PartitionScannedDocumentProvider extends FileDocumentProvider {

	private final PartitionScannerFactory partitionScannerFactory;

	public PartitionScannedDocumentProvider(
			final PartitionScannerFactory partitionScannerFactory) {
		this.partitionScannerFactory = partitionScannerFactory;
	}

	@Override
	protected IDocument createDocument(final Object element)
			throws CoreException {
		final IDocument document = super.createDocument(element);
		if (document != null) {
			attachPartitionerTo(document);
		}

		return document;
	}

	private void attachPartitionerTo(final IDocument document) {
		final IDocumentPartitioner partitioner = new FastPartitioner(
				partitionScannerFactory.createScanner(),
				partitionScannerFactory.legalContentTypes());
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
	}
}
