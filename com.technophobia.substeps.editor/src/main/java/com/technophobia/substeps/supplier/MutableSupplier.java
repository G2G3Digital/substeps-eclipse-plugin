package com.technophobia.substeps.supplier;

/**
 * Extension of {@link Supplier} that allows state to be set
 * 
 * @author sforbes
 * 
 * @param <T>
 */
public interface MutableSupplier<T> extends Supplier<T> {

    /**
     * Set the value to t
     * 
     * @param t
     *            the value to set
     */
    void set(T t);
}
