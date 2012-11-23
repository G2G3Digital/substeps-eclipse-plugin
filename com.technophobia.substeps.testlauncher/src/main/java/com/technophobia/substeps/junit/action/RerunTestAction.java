/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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
