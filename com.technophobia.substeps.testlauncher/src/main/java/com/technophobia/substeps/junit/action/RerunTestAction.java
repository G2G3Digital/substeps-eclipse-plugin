package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.TestContext;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;

public class RerunTestAction extends Action {
    private final String testId;
    private final String className;
    private final String testName;
    private final Notifier<TestContext> testRunner;
    private final String launchMode;


    public RerunTestAction(final String actionName, final Notifier<TestContext> runner, final String testId,
            final String className, final String testName, final String launchMode) {
        super(actionName);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, SubstepsHelpContextIds.RERUN_ACTION);
        this.testRunner = runner;
        this.testId = testId;
        this.className = className;
        this.testName = testName;
        this.launchMode = launchMode;
    }


    /*
     * @see IAction#run()
     */
    @Override
    public void run() {
        testRunner.notify(new TestContext(testId, className, testName, launchMode));
    }
}
