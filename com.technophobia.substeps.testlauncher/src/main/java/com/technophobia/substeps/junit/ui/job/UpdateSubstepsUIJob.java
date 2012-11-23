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
