package com.technophobia.substeps.supplier;

/**
 * Supplies a value of type t
 * 
 * @author sforbes
 * 
 * @param <T>
 */
public interface Supplier<T> {

    /**
     * Return value of type T
     * 
     * @return
     */
    T get();
}
