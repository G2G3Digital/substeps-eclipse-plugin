package com.technophobia.eclipse.ui.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface StoppableJob {

    IStatus run(final IProgressMonitor monitor);


    void stop();
}
