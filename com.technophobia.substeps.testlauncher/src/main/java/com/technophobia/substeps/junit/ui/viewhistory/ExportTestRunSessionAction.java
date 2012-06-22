package com.technophobia.substeps.junit.ui.viewhistory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.junit.model.JUnitModel;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class ExportTestRunSessionAction extends Action {
    private final TestRunSession fTestRunSession;
    private final Shell fShell;


    public ExportTestRunSessionAction(final Shell shell, final TestRunSession testRunSession) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_name);
        fShell = shell;
        fTestRunSession = testRunSession;
    }


    @Override
    public void run() {
        final FileDialog exportDialog = new FileDialog(fShell, SWT.SAVE);
        exportDialog
                .setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_title);
        final IDialogSettings dialogSettings = JUnitPlugin.getDefault().getDialogSettings();
        final String lastPath = dialogSettings.get(ImportTestRunSessionAction.PREF_LAST_PATH);
        if (lastPath != null) {
            exportDialog.setFilterPath(lastPath);
        }
        exportDialog.setFileName(getFileName());
        exportDialog.setFilterExtensions(new String[] { "*.xml", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        final String path = exportDialog.open();
        if (path == null)
            return;

        // TODO: MULTI: getFileNames()
        final File file = new File(path);

        try {
            JUnitModel.exportTestRunSession(fTestRunSession, file);
        } catch (final CoreException e) {
            JUnitPlugin.log(e);
            ErrorDialog.openError(fShell,
                    SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_title, e
                            .getStatus().getMessage(), e.getStatus());
        }
    }


    private String getFileName() {
        final String testRunName = fTestRunSession.getTestRunName();
        final long startTime = fTestRunSession.getStartTime();
        if (startTime <= 0)
            return testRunName;

        final String isoTime = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(startTime)); //$NON-NLS-1$
        return testRunName + " " + isoTime + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}