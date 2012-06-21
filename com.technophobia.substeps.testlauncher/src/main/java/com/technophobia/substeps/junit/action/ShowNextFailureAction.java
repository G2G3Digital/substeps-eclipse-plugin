package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.FeatureViewer;

public class ShowNextFailureAction extends Action {

    private final FeatureViewer featureViewer;


    public ShowNextFailureAction(final FeatureViewer featureViewer) {
        super(JUnitMessages.ShowNextFailureAction_label);
        this.featureViewer = featureViewer;

        setDisabledImageDescriptor(JUnitPlugin.getImageDescriptor("dlcl16/select_next.gif")); //$NON-NLS-1$
        setHoverImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
        setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
        setToolTipText(JUnitMessages.ShowNextFailureAction_tooltip);
    }


    @Override
    public void run() {
        featureViewer.selectFailure(true);
    }
}
