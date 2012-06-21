package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.FeatureViewer;

public class ShowPreviousFailureAction extends Action {

    private final FeatureViewer testViewer;


    public ShowPreviousFailureAction(final FeatureViewer testViewer) {
        super(JUnitMessages.ShowPreviousFailureAction_label);
        setDisabledImageDescriptor(JUnitPlugin.getImageDescriptor("dlcl16/select_prev.gif")); //$NON-NLS-1$
        setHoverImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/select_prev.gif")); //$NON-NLS-1$
        setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/select_prev.gif")); //$NON-NLS-1$
        setToolTipText(JUnitMessages.ShowPreviousFailureAction_tooltip);
        this.testViewer = testViewer;
    }


    @Override
    public void run() {
        testViewer.selectFailure(false);
    }
}
