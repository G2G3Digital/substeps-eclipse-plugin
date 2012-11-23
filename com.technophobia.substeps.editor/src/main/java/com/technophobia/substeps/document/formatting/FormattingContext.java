package com.technophobia.substeps.document.formatting;

import com.technophobia.substeps.document.content.ContentTypeDefinition;

public interface FormattingContext {

    boolean hasPreviousContent();


    ContentTypeDefinition inspectPreviousContentType() throws InvalidFormatPositionException;


    boolean hasMoreContent();


    FormattingContext impersonateNextContentContext() throws InvalidFormatPositionException;


    ContentTypeDefinition currentContentType();
}
