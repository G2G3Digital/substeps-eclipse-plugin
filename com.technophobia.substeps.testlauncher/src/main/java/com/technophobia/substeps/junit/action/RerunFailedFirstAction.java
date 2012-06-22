package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.TestRelauncher;

public class RerunFailedFirstAction extends Action {

    private final TestRelauncher testRelauncher;


    public RerunFailedFirstAction(final String actionDefinitionId, final TestRelauncher testRelauncher) {
        this.testRelauncher = testRelauncher;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunfailuresaction_label);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunfailuresaction_label);
        JUnitPlugin.setLocalImageDescriptors(this, "relaunchf.gif"); //$NON-NLS-1$
        setEnabled(false);
        setActionDefinitionId(actionDefinitionId);
    }


    @Override
    public void run() {
        testRelauncher.run();
    }

}
