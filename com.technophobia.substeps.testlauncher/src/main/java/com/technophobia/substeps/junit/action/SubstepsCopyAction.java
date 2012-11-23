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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.technophobia.substeps.junit.ui.FailureTrace;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;
import com.technophobia.substeps.model.structure.SubstepsTestElement;

public class SubstepsCopyAction extends SelectionListenerAction {
    private final FailureTrace failureTrace;

    private final Clipboard clipboard;

    private SubstepsTestElement testElement;

    private final Shell shell;


    public SubstepsCopyAction(final Shell shell, final FailureTrace failureTrace, final Clipboard clipboard) {
        super(SubstepsFeatureMessages.CopyTrace_action_label);
        Assert.isNotNull(clipboard);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, SubstepsHelpContextIds.COPYTRACE_ACTION);
        this.shell = shell;
        this.failureTrace = failureTrace;
        this.clipboard = clipboard;
    }


    /*
     * @see IAction#run()
     */
    @Override
    public void run() {
        final String trace = failureTrace.getTrace();
        String source = null;
        if (trace != null) {
            source = convertLineTerminators(trace);
        } else if (testElement != null) {
            source = testElement.getTestName();
        }
        if (source == null || source.length() == 0)
            return;

        final TextTransfer plainTextTransfer = TextTransfer.getInstance();
        try {
            clipboard
                    .setContents(new String[] { convertLineTerminators(source) }, new Transfer[] { plainTextTransfer });
        } catch (final SWTError e) {
            if (e.code != DND.ERROR_CANNOT_SET_CLIPBOARD)
                throw e;
            if (MessageDialog.openQuestion(shell, SubstepsFeatureMessages.CopyTraceAction_problem,
                    SubstepsFeatureMessages.CopyTraceAction_clipboard_busy))
                run();
        }
    }


    public void handleTestSelected(final SubstepsTestElement test) {
        this.testElement = test;
    }


    private String convertLineTerminators(final String in) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        final StringReader stringReader = new StringReader(in);
        final BufferedReader bufferedReader = new BufferedReader(stringReader);
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
        } catch (final IOException e) {
            return in; // return the trace unfiltered
        }
        return stringWriter.toString();
    }
}
