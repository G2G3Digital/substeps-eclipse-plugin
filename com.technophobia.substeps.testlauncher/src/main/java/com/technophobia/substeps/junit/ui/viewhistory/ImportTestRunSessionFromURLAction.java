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
package com.technophobia.substeps.junit.ui.viewhistory;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.TestSessionRunImporter;

public class ImportTestRunSessionFromURLAction extends Action {
    private static class URLValidator implements IInputValidator {
        @Override
        public String isValid(final String newText) {
            if (newText.length() == 0)
                return null;
            try {
                @SuppressWarnings("unused")
                final URL url = new URL(newText);
                return null;
            } catch (final MalformedURLException e) {
                return SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionFromURLAction_invalid_url
                        + e.getLocalizedMessage();
            }
        }
    }

    private static final String DIALOG_SETTINGS = "ImportTestRunSessionFromURLAction"; //$NON-NLS-1$

    private final Shell fShell;


    public ImportTestRunSessionFromURLAction(final Shell shell) {
        super(
                SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionFromURLAction_import_from_url);
        fShell = shell;
    }


    @Override
    public void run() {
        final String title = SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionAction_title;
        final String message = SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionFromURLAction_url;

        final IDialogSettings dialogSettings = FeatureRunnerPlugin.instance().getDialogSettings();
        String url = dialogSettings.get(ImportTestRunSessionAction.PREF_LAST_PATH);

        final IInputValidator validator = new URLValidator();

        final InputDialog inputDialog = new InputDialog(fShell, title, message, url, validator) {
            @Override
            protected Control createDialogArea(final Composite parent) {
                final Control dialogArea2 = super.createDialogArea(parent);
                final Object layoutData = getText().getLayoutData();
                if (layoutData instanceof GridData) {
                    final GridData gd = (GridData) layoutData;
                    gd.widthHint = convertWidthInCharsToPixels(150);
                }
                return dialogArea2;
            }


            @Override
            protected IDialogSettings getDialogBoundsSettings() {
                IDialogSettings settings = dialogSettings.getSection(DIALOG_SETTINGS);
                if (settings == null) {
                    settings = dialogSettings.addNewSection(DIALOG_SETTINGS);
                }
                settings.put("DIALOG_HEIGHT", Dialog.DIALOG_DEFAULT_BOUNDS); //$NON-NLS-1$
                return settings;
            }


            @Override
            protected boolean isResizable() {
                return true;
            }
        };

        final int res = inputDialog.open();
        if (res == IDialogConstants.OK_ID) {
            url = inputDialog.getValue();
            dialogSettings.put(ImportTestRunSessionAction.PREF_LAST_PATH, url);
            TestSessionRunImporter.importTestRunSession(url);
        }
    }
}
