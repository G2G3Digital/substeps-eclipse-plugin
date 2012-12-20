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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;

public class ExportTestRunSessionAction extends Action {
    private final SubstepsRunSession substepsRunSession;
    @SuppressWarnings("unused")
    private final Shell shell;


    public ExportTestRunSessionAction(final Shell shell, final SubstepsRunSession substepsRunSession) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_name);
        this.shell = shell;
        this.substepsRunSession = substepsRunSession;
    }


    @Override
    public void run() {
        throw new UnsupportedOperationException("Export test run is not currently available");
        /*
         * final FileDialog exportDialog = new FileDialog(shell, SWT.SAVE);
         * exportDialog .setText(SubstepsFeatureMessages.
         * SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_title);
         * final IDialogSettings dialogSettings =
         * JUnitPlugin.getDefault().getDialogSettings(); final String lastPath =
         * dialogSettings.get(ImportTestRunSessionAction.PREF_LAST_PATH); if
         * (lastPath != null) { exportDialog.setFilterPath(lastPath); }
         * exportDialog.setFileName(getFileName());
         * exportDialog.setFilterExtensions(new String[] { "*.xml", "*.*" });
         * //$NON-NLS-1$ //$NON-NLS-2$ final String path = exportDialog.open();
         * if (path == null) return;
         * 
         * // TODO: MULTI: getFileNames() final File file = new File(path);
         * 
         * try { SubstepsModel.exportTestRunSession(substepsRunSession, file); }
         * catch (final CoreException e) { FeatureRunnerPlugin.log(Status.ERROR,
         * e.getMessage()); ErrorDialog.openError(shell,
         * SubstepsFeatureMessages.
         * SubstepsFeatureTestRunnerViewPart_ExportTestRunSessionAction_title, e
         * .getStatus().getMessage(), e.getStatus()); }
         */
    }


    @SuppressWarnings("unused")
    private String getFileName() {
        final String testRunName = substepsRunSession.getTestRunName();
        final long startTime = substepsRunSession.getStartTime();
        if (startTime <= 0)
            return testRunName;

        final String isoTime = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(startTime)); //$NON-NLS-1$
        return testRunName + " " + isoTime + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
