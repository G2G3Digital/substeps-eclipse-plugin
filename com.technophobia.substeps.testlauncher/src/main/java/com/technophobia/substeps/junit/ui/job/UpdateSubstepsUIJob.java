package com.technophobia.substeps.junit.ui.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.eclipse.ui.job.StoppableJob;

public class UpdateSubstepsUIJob extends UIJob implements StoppableJob {
    private boolean running;
    private final UiUpdater uiUpdater;
    private final int refreshInterval;


    public UpdateSubstepsUIJob(final String name, final int refreshInterval, final UiUpdater uiUpdater) {
        super(name);
        this.refreshInterval = refreshInterval;
        this.uiUpdater = uiUpdater;
        this.running = true;
        setSystem(true);
    }


    @Override
    public void stop() {
        this.running = false;
    }


    @Override
    public boolean shouldSchedule() {
        return this.running;
    }


    @Override
    public IStatus runInUIThread(final IProgressMonitor monitor) {
        uiUpdater.doUpdate();

        schedule(refreshInterval);
        return Status.OK_STATUS;
    }
}
