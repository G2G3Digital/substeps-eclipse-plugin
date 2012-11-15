package com.technophobia.eclipse.supplier;

import com.technophobia.substeps.supplier.Supplier;

public class ConstantSupplier<T> implements Supplier<T> {

    private final T t;


    public ConstantSupplier(final T t) {
        this.t = t;
    }


    @Override
    public T get() {
        return t;
    }

}
