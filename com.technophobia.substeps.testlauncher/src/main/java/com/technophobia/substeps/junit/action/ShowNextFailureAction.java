package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.FeatureViewer;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class ShowNextFailureAction extends Action {

    private final FeatureViewer featureViewer;


    public ShowNextFailureAction(final FeatureViewer featureViewer) {
        super(SubstepsFeatureMessages.ShowNextFailureAction_label);
        this.featureViewer = featureViewer;

        setDisabledImageDescriptor(JUnitPlugin.getImageDescriptor("dlcl16/select_next.gif")); //$NON-NLS-1$
        setHoverImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
        setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
        setToolTipText(SubstepsFeatureMessages.ShowNextFailureAction_tooltip);
    }


    @Override
    public void run() {
        featureViewer.selectFailure(true);
    }
}
