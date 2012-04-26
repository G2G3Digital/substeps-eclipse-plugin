package com.technophobia.substeps.document.partition;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

public class DebugFastPartitioner extends FastPartitioner {

	public DebugFastPartitioner(final IPartitionTokenScanner scanner,
			final String[] legalContentTypes) {
		super(scanner, legalContentTypes);
	}

	@Override
	public void connect(final IDocument document, final boolean delayInitialise) {
		super.connect(document, delayInitialise);
		printPartitions(document);
	}

	public void printPartitions(final IDocument document) {
		final StringBuffer buffer = new StringBuffer();

		final ITypedRegion[] partitions = computePartitioning(0,
				document.getLength());
		for (int i = 0; i < partitions.length; i++) {
			try {
				buffer.append("Partition type: " + partitions[i].getType()
						+ ", offset: " + partitions[i].getOffset()
						+ ", length: " + partitions[i].getLength());
				buffer.append("\n");
				buffer.append("Text:\n");
				buffer.append(document.get(partitions[i].getOffset(),
						partitions[i].getLength()));
				buffer.append("\n---------------------------\n\n\n");
			} catch (final BadLocationException e) {
				e.printStackTrace();
			}
		}
		System.out.print(buffer);
	}
}
