package com.technophobia.substeps.model.structure;

import com.technophobia.eclipse.transformer.Transformer;

public class ReplacesConstantWithAnotherConstantValueTestNameTransformer implements Transformer<String, String> {

    private final String replace;
    private final String with;


    public ReplacesConstantWithAnotherConstantValueTestNameTransformer(final String replace, final String with) {
        this.replace = replace;
        this.with = with;
    }


    @Override
    public String to(final String from) {
        return from.equals(replace) ? with : from;
    }

}
