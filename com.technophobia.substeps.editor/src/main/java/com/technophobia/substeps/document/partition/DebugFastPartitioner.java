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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * Extension of {@link FastPartitioner} that debugs partition info
 * 
 * @author sforbes
 * 
 */
public class DebugFastPartitioner extends FastPartitioner {

    public DebugFastPartitioner(final IPartitionTokenScanner scanner, final String[] legalContentTypes) {
        super(scanner, legalContentTypes);
    }


    @Override
    public void connect(final IDocument document, final boolean delayInitialise) {
        super.connect(document, delayInitialise);
        printPartitions(document);
    }


    /**
     * iterate through partitions, outputting details to sysout
     * 
     * @param document
     */
    public void printPartitions(final IDocument document) {
        final StringBuffer buffer = new StringBuffer();

        final ITypedRegion[] partitions = computePartitioning(0, document.getLength());
        for (int i = 0; i < partitions.length; i++) {
            try {
                buffer.append("Partition type: " + partitions[i].getType() + ", offset: " + partitions[i].getOffset()
                        + ", length: " + partitions[i].getLength());
                buffer.append("\n");
                buffer.append("Text:\n");
                buffer.append(document.get(partitions[i].getOffset(), partitions[i].getLength()));
                buffer.append("\n---------------------------\n\n\n");
            } catch (final BadLocationException e) {
                e.printStackTrace();
            }
        }
        System.out.print(buffer);
    }
}
