package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.TestRelauncher;

public class RerunLastTestAction extends Action {
    private final TestRelauncher testRelauncher;


    public RerunLastTestAction(final String actionDefinitionId, final TestRelauncher testRelauncher) {
        this.testRelauncher = testRelauncher;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunaction_label);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunaction_label);
        JUnitPlugin.setLocalImageDescriptors(this, "relaunch.gif"); //$NON-NLS-1$
        setEnabled(false);
        setActionDefinitionId(actionDefinitionId);
    }


    @Override
    public void run() {
        testRelauncher.run();
    }
}
