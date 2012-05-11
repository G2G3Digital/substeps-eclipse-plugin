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
