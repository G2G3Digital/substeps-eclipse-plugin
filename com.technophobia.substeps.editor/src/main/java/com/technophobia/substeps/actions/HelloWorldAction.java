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
