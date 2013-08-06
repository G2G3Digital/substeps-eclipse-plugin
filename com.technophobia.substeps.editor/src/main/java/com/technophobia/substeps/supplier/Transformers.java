package com.technophobia.substeps.supplier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Transformers {

    private Transformers() {
    }


    public static final <T, S> List<S> transform(final Iterable<T> values, Transformer<T, S> t) {
        final List<S> result = new ArrayList<S>();
        for (final Iterator<T> i = values.iterator(); i.hasNext();) {
            result.add(t.from(i.next()));
        }
        return result;
    }

}
