package com.technophobia.substeps.document.content;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.supplier.Supplier;

public interface ContentTypeDefinition {

	String id();

	IPredicateRule partitionRule();

	IRule damageRepairerRule(ColourManager colourManager);

	IFormattingStrategy formattingStrategy(
			Supplier<FormattingContext> formattingContextSupplier);
}
