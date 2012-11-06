package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.FixedIndentFormattingStrategy;
import com.technophobia.substeps.supplier.Supplier;

public class ThenContentTypeDefinition extends AbstractKeywordContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__feature_then";
    public static final String PREFIX_TEXT = "Then";


    public ThenContentTypeDefinition() {
        super(CONTENT_TYPE_ID, PREFIX_TEXT, false);
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new FixedIndentFormattingStrategy("\t ", formattingContextSupplier);
    }

}
