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
