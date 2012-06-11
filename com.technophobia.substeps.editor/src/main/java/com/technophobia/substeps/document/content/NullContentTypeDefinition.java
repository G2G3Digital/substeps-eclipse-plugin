package com.technophobia.substeps.document.content;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.NullFormattingStrategy;
import com.technophobia.substeps.supplier.Supplier;

public class NullContentTypeDefinition implements ContentTypeDefinition {

	@Override
	public String id() {
		return "";
	}
	
	@Override
	public boolean isOptional() {
		return false;
	}

	@Override
	public IPredicateRule partitionRule() {
		return null;
	}

	@Override
	public IRule damageRepairerRule(ColourManager colourManager) {
		return null;
	}

	@Override
	public IFormattingStrategy formattingStrategy(
			Supplier<FormattingContext> formattingContextSupplier) {
		return new NullFormattingStrategy();
	}

}
