package com.technophobia.substeps.junit.ui.viewhistory;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;

public class ClearAction extends Action {
    private final RunnerViewHistory viewHistory;


    public ClearAction(final RunnerViewHistory viewHistory) {
        this.viewHistory = viewHistory;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_clear_history_label);

        boolean enabled = false;
        final List<SubstepsRunSession> substepsRunSessions = FeatureRunnerPlugin.instance().getModel()
                .getTestRunSessions();
        for (final SubstepsRunSession substepsRunSession : substepsRunSessions) {
            if (!substepsRunSession.isRunning() && !substepsRunSession.isStarting()) {
                enabled = true;
                break;
            }
        }
        setEnabled(enabled);
    }


    @Override
    public void run() {
        final List<SubstepsRunSession> substepsRunSessions = getRunningSessions();
        final SubstepsRunSession first = substepsRunSessions.isEmpty() ? null : substepsRunSessions.get(0);
        viewHistory.setHistoryEntries(substepsRunSessions, first);
    }


    private List<SubstepsRunSession> getRunningSessions() {
        final List<SubstepsRunSession> substepsRunSessions = FeatureRunnerPlugin.instance().getModel()
                .getTestRunSessions();
        for (final Iterator<SubstepsRunSession> iter = substepsRunSessions.iterator(); iter.hasNext();) {
            final SubstepsRunSession substepsRunSession = iter.next();
            if (!substepsRunSession.isRunning() && !substepsRunSession.isStarting()) {
                iter.remove();
            }
        }
        return substepsRunSessions;
    }
}