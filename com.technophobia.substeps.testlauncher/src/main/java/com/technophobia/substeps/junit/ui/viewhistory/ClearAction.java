package com.technophobia.substeps.junit.ui.viewhistory;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.ui.viewsupport.ViewHistory;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class ClearAction extends Action {
    private final ViewHistory<TestRunSession> viewHistory;


    public ClearAction(final ViewHistory<TestRunSession> viewHistory) {
        this.viewHistory = viewHistory;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_clear_history_label);

        boolean enabled = false;
        final List<TestRunSession> testRunSessions = JUnitCorePlugin.getModel().getTestRunSessions();
        for (final TestRunSession testRunSession : testRunSessions) {
            if (!testRunSession.isRunning() && !testRunSession.isStarting()) {
                enabled = true;
                break;
            }
        }
        setEnabled(enabled);
    }


    @Override
    public void run() {
        final List<TestRunSession> testRunSessions = getRunningSessions();
        final TestRunSession first = testRunSessions.isEmpty() ? null : testRunSessions.get(0);
        viewHistory.setHistoryEntries(testRunSessions, first);
    }


    private List<TestRunSession> getRunningSessions() {
        final List<TestRunSession> testRunSessions = JUnitCorePlugin.getModel().getTestRunSessions();
        for (final Iterator<TestRunSession> iter = testRunSessions.iterator(); iter.hasNext();) {
            final TestRunSession testRunSession = iter.next();
            if (!testRunSession.isRunning() && !testRunSession.isStarting()) {
                iter.remove();
            }
        }
        return testRunSessions;
    }
}