package com.technophobia.eclipse.launcher.exception;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
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
        ExceptionHandler.handle(ex, parentShell, dialogTitle, message);
    }

}
