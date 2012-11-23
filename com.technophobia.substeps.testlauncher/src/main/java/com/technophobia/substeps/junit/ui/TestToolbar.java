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
