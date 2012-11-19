package com.technophobia.substeps.supplier;

/**
 * Interface to perform some logic on an object
 * 
 * @author sforbes
 * 
 * @param <T>
 */
public interface Callback2<S, T> {

    /**
     * Do logic
     * 
     * @param s
     *            the 1st domain item
     * @param t
     *            the 2nd domain item
     */
    void doCallback(S s, T t);
}
