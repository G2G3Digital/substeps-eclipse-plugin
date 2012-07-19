package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.TestRelauncher;

public class RerunFailedFirstAction extends Action {

    private final TestRelauncher testRelauncher;


    public RerunFailedFirstAction(final String actionDefinitionId, final TestRelauncher testRelauncher,
            final SubstepsIconProvider iconProvider) {
        this.testRelauncher = testRelauncher;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunfailuresaction_label);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunfailuresaction_label);
        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchFailedDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchFailedEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchFailedEnabled)); //$NON-NLS-1$
        setEnabled(false);
        setActionDefinitionId(actionDefinitionId);
    }


    @Override
    public void run() {
        testRelauncher.run();
    }

}
