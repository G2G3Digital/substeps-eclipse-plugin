/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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
