package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.TestRelauncher;

public class RerunFailedFirstAction extends Action {

    private final TestRelauncher testRelauncher;


    public RerunFailedFirstAction(final String actionDefinitionId, final TestRelauncher testRelauncher) {
        this.testRelauncher = testRelauncher;
        setText(JUnitMessages.TestRunnerViewPart_rerunfailuresaction_label);
        setToolTipText(JUnitMessages.TestRunnerViewPart_rerunfailuresaction_tooltip);
        JUnitPlugin.setLocalImageDescriptors(this, "relaunchf.gif"); //$NON-NLS-1$
        setEnabled(false);
        setActionDefinitionId(actionDefinitionId);
    }


    @Override
    public void run() {
        testRelauncher.run();
    }

}
