package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.TestRelauncher;

public class RerunLastTestAction extends Action {
    private final TestRelauncher testRelauncher;


    public RerunLastTestAction(final String actionDefinitionId, final TestRelauncher testRelauncher) {
        this.testRelauncher = testRelauncher;
        setText(JUnitMessages.TestRunnerViewPart_rerunaction_label);
        setToolTipText(JUnitMessages.TestRunnerViewPart_rerunaction_tooltip);
        JUnitPlugin.setLocalImageDescriptors(this, "relaunch.gif"); //$NON-NLS-1$
        setEnabled(false);
        setActionDefinitionId(actionDefinitionId);
    }


    @Override
    public void run() {
        testRelauncher.run();
    }
}
