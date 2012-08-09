package com.technophobia.substeps.supplier;

/**
 * Transform a value of type From to type To
 * 
 * @author sforbes
 * 
 * @param <From>
 * @param <To>
 */
public interface Transformer<From, To> {

    /**
     * Transforms value from to to
     * 
     * @param from
     *            The item to transform from
     * @return the transformed item
     */
    To from(From from);
}
