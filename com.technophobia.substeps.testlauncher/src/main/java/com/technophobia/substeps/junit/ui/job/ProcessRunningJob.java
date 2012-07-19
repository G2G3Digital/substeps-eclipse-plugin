package com.technophobia.substeps.junit.ui.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.Job;

import com.technophobia.eclipse.ui.job.StoppableJob;
import com.technophobia.substeps.junit.ui.IMarker;

public class ProcessRunningJob extends Job implements StoppableJob {

    private final ILock processLock;
    private final IMarker familyMarker;


    public ProcessRunningJob(final String name, final IMarker familyMarker, final ILock processLock) {
        super(name);
        this.familyMarker = familyMarker;
        this.processLock = processLock;
        setSystem(true);

        // TODO: enhance - previously the lock was created in
        // TestRunnerViewPart, and then
        // acquired immediately - is this needed?
        this.processLock.acquire();
    }


    @Override
    public IStatus run(final IProgressMonitor monitor) {
        // wait until the test run terminates
        processLock.acquire();
        return Status.OK_STATUS;
    }


    @Override
    public void stop() {
        processLock.release();
    }


    @Override
    public boolean belongsTo(final Object family) {
        return family == familyMarker;
    }
}
