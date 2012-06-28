package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.component.FeatureViewer;

public class ShowNextFailureAction extends Action {

    private final FeatureViewer featureViewer;


    public ShowNextFailureAction(final FeatureViewer featureViewer, final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.ShowNextFailureAction_label);
        this.featureViewer = featureViewer;

        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.SelectNextTestDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.SelectNextTestEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.SelectNextTestEnabled)); //$NON-NLS-1$
        setToolTipText(SubstepsFeatureMessages.ShowNextFailureAction_tooltip);
    }


    @Override
    public void run() {
        featureViewer.selectFailure(true);
    }
}
