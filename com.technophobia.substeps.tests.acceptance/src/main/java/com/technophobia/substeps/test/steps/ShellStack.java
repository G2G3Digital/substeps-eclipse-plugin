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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;

public class ShellStack {

    private final Stack<Shell> shells = new Stack<Shell>();


    public void dialogHasOpened(final SWTBot bot) {
        shells.push(findChildOfCurrent(bot));
    }


    public void dialogHasClosed() {
        if (shells.size() > 0) {
            shells.pop();
        } else {
            throw new IllegalStateException("Could not find parent shell of shell " + currentShell().getText());
        }
    }


    public void resetTo(final Shell parentShell) {
        clear();
        shells.push(parentShell);
    }


    public void clear() {
        shells.clear();
    }


    public Shell currentShell() {
        return shells.peek();
    }


    public int visibleShellSize(final SWTBot bot) {
        return UIThreadRunnable.syncExec(new IntResult() {

            @Override
            public Integer run() {
                final List<String> visibleShells = new ArrayList<String>();
                final Shell[] allShells = bot.getDisplay().getShells();
                for (final Shell shell : allShells) {
                    if (shell.isVisible())
                        visibleShells.add(shell.getText());
                }
                return Integer.valueOf(visibleShells.size());
            }
        }).intValue();
    }


    public Shell findChildOfCurrent(final SWTBot bot) {
        if (shells.isEmpty()) {
            throw new WidgetNotFoundException("Could not find child of current shell, as it is null");
        }

        final Display display = bot.getDisplay();

        return UIThreadRunnable.syncExec(new WidgetResult<Shell>() {
            @Override
            public Shell run() {
                final Shell[] currentShells = display.getShells();
                final Collection<Shell> childShells = new ArrayList<Shell>();
                final Shell currentShell = currentShell();
                for (final Shell shell : currentShells) {
                    if (shell.isVisible() && shell.getParent() != null
                            && shell.getParent().handle == currentShell.handle) {
                        childShells.add(shell);
                    }
                }

                if (childShells.isEmpty()) {
                    throw new WidgetNotFoundException("Could not find any child shells of " + currentShell);
                }
                if (childShells.size() > 1) {
                    throw new WidgetNotFoundException("Expected exactly 1 child of " + currentShell
                            + ", but instead found " + childShells.size());
                }
                return childShells.iterator().next();
            }
        });

    }
}
