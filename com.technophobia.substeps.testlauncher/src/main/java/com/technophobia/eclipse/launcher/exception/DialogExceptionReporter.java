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
