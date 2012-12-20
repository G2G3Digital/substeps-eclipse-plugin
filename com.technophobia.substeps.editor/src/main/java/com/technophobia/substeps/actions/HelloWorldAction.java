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
package com.technophobia.substeps.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * HelloWorldAction is a simple example of using an action set to extend the
 * Eclipse Workbench with a menu and toolbar action that prints the
 * "Hello World" message.
 */
public class HelloWorldAction implements IWorkbenchWindowActionDelegate {
    IWorkbenchWindow activeWindow = null;


    /**
     * Run the action. Display the Hello World message
     */
    @Override
    public void run(final IAction proxyAction) {
        // proxyAction has UI information from manifest file (ignored)
        final Shell shell = activeWindow.getShell();
        MessageDialog.openInformation(shell, "Hello World", "Hello World!");
    }


    // IActionDelegate method
    @Override
    public void selectionChanged(final IAction proxyAction, final ISelection selection) {
        // do nothing, action is not dependent on the selection
    }


    // IWorkbenchWindowActionDelegate method
    @Override
    public void init(final IWorkbenchWindow window) {
        activeWindow = window;
    }


    // IWorkbenchWindowActionDelegate method
    @Override
    public void dispose() {
        // nothing to do
    }
}
