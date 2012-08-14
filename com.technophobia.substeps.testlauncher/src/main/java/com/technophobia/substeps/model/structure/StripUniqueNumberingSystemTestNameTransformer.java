package com.technophobia.substeps.model.structure;

import com.technophobia.substeps.supplier.Transformer;

public class StripUniqueNumberingSystemTestNameTransformer implements Transformer<String, String> {

    @Override
    public String from(final String from) {
        final String[] split = from.split(":");
        if (split.length > 0 && isUniqueNumber(split[0])) {
            return from.substring(split[0].length() + 1).trim();
        }
        return from;
    }


    private boolean isUniqueNumber(final String text) {
        return text.matches("(-|[0-9])*");
    }

}
