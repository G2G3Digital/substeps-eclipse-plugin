package com.technophobia.substeps.document.partition;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.technophobia.substeps.supplier.Supplier;

/**
 * Factory interface for {@link IPartitionTokenScanner}
 * 
 * @author sforbes
 * 
 */
public interface PartitionScannerFactory {

    /**
     * Create a new {@link IPartitionTokenScanner}
     * 
     * @return the newly created scanner
     */
    IPartitionTokenScanner createScanner(final Supplier<PartitionContext> partitionContextSupplier);


    /**
     * All legal content types
     * 
     * @return
     */
    String[] legalContentTypes();
}
