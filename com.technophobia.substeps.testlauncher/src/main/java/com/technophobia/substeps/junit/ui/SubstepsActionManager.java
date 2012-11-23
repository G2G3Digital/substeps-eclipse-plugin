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

public class SubstepsActionManager {

    private final Action stopAction;
    private final Action copyAction;
    private final Action rerunFailedFirstAction;
    private final Action rerunLastTestAction;
    private final Action nextAction;
    private final Action prevAction;


    public SubstepsActionManager(final Action stopAction, final Action copyAction, final Action rerunFailedFirstAction,
            final Action rerunLastTestAction, final Action nextAction, final Action prevAction) {
        this.nextAction = nextAction;
        this.prevAction = prevAction;
        this.stopAction = stopAction;
        this.copyAction = copyAction;
        this.rerunFailedFirstAction = rerunFailedFirstAction;
        this.rerunLastTestAction = rerunLastTestAction;
    }


    public Action nextAction() {
        return nextAction;
    }


    public Action prevAction() {
        return prevAction;
    }


    public Action stopAction() {
        return stopAction;
    }


    public Action copyAction() {
        return copyAction;
    }


    public Action rerunFailedFirstAction() {
        return rerunFailedFirstAction;
    }


    public Action rerunLastTestAction() {
        return rerunLastTestAction;
    }


    public void setPrevActionEnabled(final boolean enabled) {
        setEnabled(prevAction, enabled);
    }


    public void setNextActionEnabled(final boolean enabled) {
        setEnabled(nextAction, enabled);
    }


    public void setStopActionEnabled(final boolean enabled) {
        setEnabled(stopAction, enabled);
    }


    public void setRerunFailedFirstActionEnabled(final boolean enabled) {
        setEnabled(rerunFailedFirstAction, enabled);
    }


    public void setRerunLastTestActionEnabled(final boolean enabled) {
        setEnabled(rerunLastTestAction, enabled);
    }


    private void setEnabled(final Action action, final boolean enabled) {
        action.setEnabled(enabled);
    }
}
