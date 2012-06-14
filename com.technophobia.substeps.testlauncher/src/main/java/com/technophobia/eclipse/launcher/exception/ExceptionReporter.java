package com.technophobia.eclipse.launcher.exception;

import org.eclipse.core.runtime.CoreException;

public interface ExceptionReporter {

    void report(CoreException ex);

}
