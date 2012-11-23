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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.TestSessionRunImporter;

public class SubstepsPasteAction extends Action {
    private final Shell shell;
    private final Clipboard clipboard;


    public SubstepsPasteAction(final Shell shell, final Clipboard clipboard) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_JUnitPasteAction_label);
        Assert.isNotNull(clipboard);
        this.shell = shell;
        this.clipboard = clipboard;
    }


    @Override
    public void run() {
        String urlData = (String) clipboard.getContents(URLTransfer.getInstance());
        if (urlData == null) {
            urlData = (String) clipboard.getContents(TextTransfer.getInstance());
        }
        if (urlData != null && urlData.length() > 0) {
            if (isValidUrl(urlData)) {
                TestSessionRunImporter.importTestRunSession(urlData);
                return;
            }
        }
        MessageDialog.openInformation(shell,
                SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_JUnitPasteAction_cannotpaste_title,
                SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_JUnitPasteAction_cannotpaste_message);
    }


    private boolean isValidUrl(final String urlData) {
        try {
            @SuppressWarnings("unused")
            final URL url = new URL(urlData);
        } catch (final MalformedURLException e) {
            return false;
        }
        return true;
    }
}
