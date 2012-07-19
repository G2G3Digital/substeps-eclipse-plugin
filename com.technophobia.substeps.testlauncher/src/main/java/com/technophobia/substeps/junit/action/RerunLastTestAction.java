package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.TestRelauncher;

public class RerunLastTestAction extends Action {
    private final TestRelauncher testRelauncher;


    public RerunLastTestAction(final String actionDefinitionId, final TestRelauncher testRelauncher,
            final SubstepsIconProvider iconProvider) {
        this.testRelauncher = testRelauncher;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunaction_label);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunaction_label);
        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchEnabled)); //$NON-NLS-1$
        setEnabled(false);
        setActionDefinitionId(actionDefinitionId);
    }


    @Override
    public void run() {
        testRelauncher.run();
    }
}
