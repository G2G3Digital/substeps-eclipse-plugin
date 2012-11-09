package com.technophobia.substeps.document.content.feature.partiton;

public interface TextProcessor<Result> {

    Result doWithText(String text);
}
