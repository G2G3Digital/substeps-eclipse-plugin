package com.technophobia.substeps.junit.action;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.junit.ui.TestSessionRunImporter;

public class SubstepsPasteAction extends Action {
    private final Shell shell;
    private final Clipboard clipboard;


    public SubstepsPasteAction(final Shell shell, final Clipboard clipboard) {
        super(JUnitMessages.TestRunnerViewPart_JUnitPasteAction_label);
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
        MessageDialog.openInformation(shell, JUnitMessages.TestRunnerViewPart_JUnitPasteAction_cannotpaste_title,
                JUnitMessages.TestRunnerViewPart_JUnitPasteAction_cannotpaste_message);
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
