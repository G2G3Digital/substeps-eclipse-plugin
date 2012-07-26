package com.technophobia.substeps.junit.ui;

import org.eclipse.jface.action.Action;

import com.technophobia.eclipse.ui.UiUpdater;

public class TestToolbar implements UiUpdater {
    private final Action nextFailureAction;
    private final Action previousFailutreAction;

    private final TestRunStats testRunStats;


    public TestToolbar(final Action nextFailureAction, final Action previousFailutreAction,
            final TestRunStats testRunStats) {
        this.nextFailureAction = nextFailureAction;
        this.previousFailutreAction = previousFailutreAction;
        this.testRunStats = testRunStats;
    }


    @Override
    public void doUpdate() {
        final boolean hasErrorsOrFailures = testRunStats.hasErrorsOrFailures();
        nextFailureAction.setEnabled(hasErrorsOrFailures);
        previousFailutreAction.setEnabled(hasErrorsOrFailures);
    }


    @Override
    public void reset() {
        // No-op
    }
}
