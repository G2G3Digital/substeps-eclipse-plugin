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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class ImportTestRunSessionAction extends Action {

    public static final String PREF_LAST_PATH = "lastImportExportPath";

    @SuppressWarnings("unused")
    private final Shell shell;


    public ImportTestRunSessionAction(final Shell shell) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionAction_name);
        this.shell = shell;
    }


    @Override
    public void run() {
        throw new UnsupportedOperationException("Not yet implemented");
        /*
         * final FileDialog importDialog = new FileDialog(fShell, SWT.OPEN);
         * importDialog .setText(SubstepsFeatureMessages.
         * SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionAction_title);
         * final IDialogSettings dialogSettings =
         * JUnitPlugin.getDefault().getDialogSettings(); final String lastPath =
         * dialogSettings.get(PREF_LAST_PATH); if (lastPath != null) {
         * importDialog.setFilterPath(lastPath); }
         * importDialog.setFilterExtensions(new String[] { "*.xml", "*.*" });
         * //$NON-NLS-1$ //$NON-NLS-2$ final String path = importDialog.open();
         * if (path == null) return;
         * 
         * // TODO: MULTI: getFileNames() final File file = new File(path);
         * 
         * try { SubstepsModel.importTestRunSession(file); } catch (final
         * CoreException e) { JUnitPlugin.log(e); ErrorDialog.openError(fShell,
         * SubstepsFeatureMessages
         * .SubstepsFeatureTestRunnerViewPart_ImportTestRunSessionAction_title,
         * e .getStatus().getMessage(), e.getStatus()); }
         */
    }
}
