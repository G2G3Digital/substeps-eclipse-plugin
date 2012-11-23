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
package com.technophobia.substeps.test.component.general;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

import com.technophobia.substeps.test.component.AbstractSWTLocatable;
import com.technophobia.substeps.test.component.SWTRootComponent;
import com.technophobia.substeps.test.steps.SWTBotInitialiser;
import com.technophobia.substeps.test.steps.ShellStack;

public class GeneralDialogSWTComponent extends AbstractSWTLocatable<SWTBot> implements SWTRootComponent<SWTBot> {

    private final ShellStack shellStack;
    private final String dialogName;


    public GeneralDialogSWTComponent(final String dialogName) {
        this.dialogName = dialogName;
        this.shellStack = SWTBotInitialiser.shellStack();
    }


    //
    // Commands
    //

    // probably needs moving - not specific to dialogs
    public void clickButton(final String buttonText) {
        locate().button(buttonText).click();
    }


    public void setFocus() {
        System.out.println("Gaining focus to " + dialogName);
        final SWTBot bot = locate();
        shellStack.dialogHasOpened(bot);
        final SWTBotShell shell = new SWTBotShell(shellStack.currentShell());
        shell.activate();
        // SWTTestUtil.setActiveShellHack(shell.widget);
        shell.setFocus();
    }


    public void loseFocus() {
        System.out.println("Losing focus to " + dialogName);
        shellStack.dialogHasClosed();

        try {
            Thread.sleep(2000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public TreeSWTComponent tree() {
        return new TreeSWTComponent(this);
    }


    public ListSWTComponent list() {
        return new ListSWTComponent(this);
    }


    public TableSWTComponent table() {
        return new TableSWTComponent(this);
    }


    //
    // Queries
    //
    public boolean isDialogPresent(final String dialogTitle) {
        try {
            locate();
            return true;
        } catch (final TimeoutException ex) {
            return false;
        }
    }


    @Override
    public SWTBot doLocate() {
        // If there are 2 shells open, then we are ready (in this, we assume no
        // more than 1 dialog can be open at any time
        final SWTWorkbenchBot bot = SWTBotInitialiser.bot();
        final int visibleShellSize = shellStack.visibleShellSize(bot);
        if (visibleShellSize > 1) {
            return bot;
        }

        return null;
    }
}
