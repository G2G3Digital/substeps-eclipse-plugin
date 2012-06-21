package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.technophobia.substeps.junit.ui.FeatureViewer;

public class ShowTimeAction extends Action {

    private final FeatureViewer testViewer;


    public ShowTimeAction(final FeatureViewer testViewer) {
        super(JUnitMessages.TestRunnerViewPart_show_execution_time, IAction.AS_CHECK_BOX);
        this.testViewer = testViewer;
    }


    @Override
    public void run() {
        testViewer.setShowTime(isChecked());
    }
}
