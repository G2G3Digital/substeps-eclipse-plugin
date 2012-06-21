package com.technophobia.substeps.junit.ui.viewhistory;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.junit.model.JUnitModel;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ImportTestRunSessionAction extends Action {

    public static final String PREF_LAST_PATH = "lastImportExportPath";

    private final Shell fShell;


    public ImportTestRunSessionAction(final Shell shell) {
        super(JUnitMessages.TestRunnerViewPart_ImportTestRunSessionAction_name);
        fShell = shell;
    }


    @Override
    public void run() {
        final FileDialog importDialog = new FileDialog(fShell, SWT.OPEN);
        importDialog.setText(JUnitMessages.TestRunnerViewPart_ImportTestRunSessionAction_title);
        final IDialogSettings dialogSettings = JUnitPlugin.getDefault().getDialogSettings();
        final String lastPath = dialogSettings.get(PREF_LAST_PATH);
        if (lastPath != null) {
            importDialog.setFilterPath(lastPath);
        }
        importDialog.setFilterExtensions(new String[] { "*.xml", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
        final String path = importDialog.open();
        if (path == null)
            return;

        // TODO: MULTI: getFileNames()
        final File file = new File(path);

        try {
            JUnitModel.importTestRunSession(file);
        } catch (final CoreException e) {
            JUnitPlugin.log(e);
            ErrorDialog.openError(fShell, JUnitMessages.TestRunnerViewPart_ImportTestRunSessionAction_error_title, e
                    .getStatus().getMessage(), e.getStatus());
        }
    }
}
