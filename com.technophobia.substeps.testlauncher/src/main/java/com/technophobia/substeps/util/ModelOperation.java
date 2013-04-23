package com.technophobia.substeps.util;

import org.eclipse.core.runtime.CoreException;

public interface ModelOperation<T> {

    void doOperationOn(T t) throws CoreException;
}
