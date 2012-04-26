package com.technophobia.substeps.document.partition;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;

public interface PartitionScannerFactory {

	IPartitionTokenScanner createScanner();

	String[] legalContentTypes();
}
