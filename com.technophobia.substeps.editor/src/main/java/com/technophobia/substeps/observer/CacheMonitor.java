package com.technophobia.substeps.observer;

import org.eclipse.core.resources.IProject;

public interface CacheMonitor<T> {

    void refreshCacheFor(T t);


    void evictFrom(IProject project);
}
