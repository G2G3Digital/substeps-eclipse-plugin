package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.component.FeatureViewer;

public class ShowTimeAction extends Action {

    private final FeatureViewer testViewer;


    public ShowTimeAction(final FeatureViewer testViewer) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_show_execution_time, IAction.AS_CHECK_BOX);
        this.testViewer = testViewer;
    }


    @Override
    public void run() {
        testViewer.setShowTime(isChecked());
    }
}
