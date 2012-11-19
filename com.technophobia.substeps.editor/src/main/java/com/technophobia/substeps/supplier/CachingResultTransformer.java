package com.technophobia.substeps.supplier;

import com.technophobia.substeps.observer.CacheMonitor;

/**
 * Bridge interface between {@link Transformer} and {@link CacheMonitor}
 * 
 * @author sforbes
 * 
 * @param <From>
 * @param <To>
 */
public interface CachingResultTransformer<From, To> extends Transformer<From, To>, CacheMonitor<From> {
    // Empty signature
}
