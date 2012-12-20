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
package com.technophobia.substeps.test.steps;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.ui.PlatformUI;

import com.technophobia.substeps.runner.setupteardown.Annotations.AfterEveryFeature;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeAllFeatures;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeEveryFeature;

public class SWTBotInitialiser {

    private static SWTWorkbenchBot bot;
    private static ShellStack shellStack = new ShellStack();


    @BeforeAllFeatures
    public void initialiseSWTBot() {
        bot = new SWTWorkbenchBot();
    }


    @BeforeEveryFeature
    public void setUp() {
        final Shell platformShell = UIThreadRunnable.syncExec(new WidgetResult<Shell>() {

            @Override
            public Shell run() {
                final Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                if (activeShell == null) {
                    final Shell[] shells = PlatformUI.getWorkbench().getDisplay().getShells();
                    for (final Shell shell : shells) {
                        if (shell.isVisible()) {
                            shell.setActive();
                            shell.forceActive();
                            return shell;
                        }
                    }
                }
                return activeShell;

            }
        });
        shellStack.resetTo(platformShell);
    }


    @AfterEveryFeature
    public void tearDown() {
        shellStack.clear();
    }


    public static SWTWorkbenchBot bot() {
        return bot;
    }


    public static ShellStack shellStack() {
        return shellStack;
    }

}
