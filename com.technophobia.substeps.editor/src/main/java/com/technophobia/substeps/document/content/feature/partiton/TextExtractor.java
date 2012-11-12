package com.technophobia.substeps.document.content.feature.partiton;

public interface TextExtractor<TextSource, Result> {

    Result extractText(TextSource textSource);
}
