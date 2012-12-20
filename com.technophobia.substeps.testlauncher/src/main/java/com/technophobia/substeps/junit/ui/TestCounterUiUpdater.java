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
package com.technophobia.substeps.junit.ui;

import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.substeps.junit.ui.TestRunStats.TestRunState;
import com.technophobia.substeps.junit.ui.component.CounterPanel;
import com.technophobia.substeps.junit.ui.component.ProgressBar;
import com.technophobia.substeps.supplier.Supplier;

public class TestCounterUiUpdater implements UiUpdater {

    private final CounterPanel counterPanel;
    private final ProgressBar progressBar;
    private final Supplier<TestRunStats> testRunStatsSupplier;


    public TestCounterUiUpdater(final Supplier<TestRunStats> testRunStatsSupplier, final CounterPanel counterPanel,
            final ProgressBar progressBar) {
        this.testRunStatsSupplier = testRunStatsSupplier;
        this.counterPanel = counterPanel;
        this.progressBar = progressBar;
    }


    @Override
    public void doUpdate() {
        // TODO: Inefficient. Either
        // - keep a boolean fHasTestRun and update only on changes, or
        // - improve components to only redraw on changes (once!).

        final TestRunStats testRunStats = testRunStatsSupplier.get();

        // TODO: hack - remember to use NullTestRunSession
        // if (fTestRunSession != null) {
        // startedCount = testRunSession.getStartedCount();
        // ignoredCount = testRunSession.getIgnoredCount();
        // totalCount = fTestRunSession.getTotalCount();
        // errorCount = fTestRunSession.getErrorCount();
        // failureCount = fTestRunSession.getFailureCount();
        // hasErrorsOrFailures = errorCount + failureCount > 0;
        // stopped = fTestRunSession.isStopped();
        // } else {
        // startedCount = 0;
        // ignoredCount = 0;
        // totalCount = 0;
        // errorCount = 0;
        // failureCount = 0;
        // hasErrorsOrFailures = false;
        // stopped = false;
        // }

        final int startedCount = testRunStats.getStartedCount();
        final int totalCount = testRunStats.getTotalCount();
        final int errorCount = testRunStats.getErrorCount();
        final int failureCount = testRunStats.getFailureCount();
        final TestRunState state = testRunStats.getState();

        counterPanel.setTotal(totalCount);
        counterPanel.setRunValue(startedCount, testRunStats.getIgnoredCount());
        counterPanel.setErrorValue(errorCount);
        counterPanel.setFailureValue(failureCount);

        int ticksDone;
        if (startedCount == 0)
            ticksDone = 0;
        else {
            if (startedCount == totalCount && !state.equals(TestRunState.IN_PROGRESS))
                ticksDone = totalCount;
            else
                ticksDone = startedCount - 1;
        }

        final boolean hasErrorsOrFailures = errorCount + failureCount > 0;
        progressBar.reset(hasErrorsOrFailures, state.equals(TestRunState.STOPPED), ticksDone, totalCount);
    }


    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }
}
