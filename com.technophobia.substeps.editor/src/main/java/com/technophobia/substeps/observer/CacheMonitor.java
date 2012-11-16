package com.technophobia.substeps.observer;

public interface CacheMonitor<T> {

    void refreshCacheFor(T t);
}
