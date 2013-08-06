package com.technophobia.substeps.supplier;

public class DefaultMutableItemContainer<T> implements MutableSupplier<T> {

    private T t = null;


    @Override
    public T get() {
        return t;
    }


    @Override
    public void set(final T t) {
        this.t = t;
    }

}
