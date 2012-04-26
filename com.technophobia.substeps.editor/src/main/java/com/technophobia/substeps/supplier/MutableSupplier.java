package com.technophobia.substeps.supplier;

public interface MutableSupplier<T> extends Supplier<T> {

	void set(T t);
}
