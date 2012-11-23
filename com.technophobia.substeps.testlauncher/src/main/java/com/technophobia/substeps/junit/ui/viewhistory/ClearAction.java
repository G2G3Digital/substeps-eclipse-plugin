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
