package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.component.FeatureViewer;

public class ShowNextFailureAction extends Action {

    private final FeatureViewer featureViewer;


    public ShowNextFailureAction(final FeatureViewer featureViewer, final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.ShowNextFailureAction_label);
        this.featureViewer = featureViewer;

        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.SelectNextTestDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.SelectNextTestEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.SelectNextTestEnabled)); //$NON-NLS-1$
        setToolTipText(SubstepsFeatureMessages.ShowNextFailureAction_tooltip);
    }


    @Override
    public void run() {
        featureViewer.selectFailure(true);
    }
}
