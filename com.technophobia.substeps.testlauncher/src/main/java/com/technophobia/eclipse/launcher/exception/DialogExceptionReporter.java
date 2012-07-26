package com.technophobia.eclipse.launcher.exception;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.FeatureRunnerPlugin;

public class DialogExceptionReporter implements ExceptionReporter {

    private final Shell parentShell;
    private final String dialogTitle;
    private final String message;


    public DialogExceptionReporter(final Shell parentShell, final String dialogTitle, final String message) {
        this.parentShell = parentShell;
        this.dialogTitle = dialogTitle;
        this.message = message;
    }


    @Override
    public void report(final CoreException ex) {

        ErrorDialog.openError(parentShell, dialogTitle, message, new Status(IStatus.ERROR,
                FeatureRunnerPlugin.PLUGIN_ID, message, ex));
    }

}
