package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.component.FeatureViewer;

public class ShowPreviousFailureAction extends Action {

    private final FeatureViewer testViewer;


    public ShowPreviousFailureAction(final FeatureViewer testViewer, final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.ShowPreviousFailureAction_label);
        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.SelectPreviousTestDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.SelectPreviousTestEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.SelectPreviousTestEnabled)); //$NON-NLS-1$
        setToolTipText(SubstepsFeatureMessages.ShowPreviousFailureAction_tooltip);
        this.testViewer = testViewer;
    }


    @Override
    public void run() {
        testViewer.selectFailure(false);
    }
}
