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
