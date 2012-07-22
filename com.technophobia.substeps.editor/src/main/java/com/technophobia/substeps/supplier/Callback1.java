package com.technophobia.substeps.supplier;

/**
 * Interface to perform some logic on an object
 * 
 * @author sforbes
 * 
 * @param <T>
 */
public interface Callback1<T> {

    /**
     * Do logic
     * 
     * @param t
     *            the domain item
     */
    void doCallback(T t);
}
