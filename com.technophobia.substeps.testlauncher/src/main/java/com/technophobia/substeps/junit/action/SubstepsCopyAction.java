package com.technophobia.substeps.junit.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.internal.junit.model.TestElement;
import org.eclipse.jdt.internal.junit.ui.IJUnitHelpContextIds;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
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

public class SubstepsCopyAction extends SelectionListenerAction {
    private final FailureTrace failureTrace;

    private final Clipboard clipboard;

    private TestElement testElement;

    private final Shell shell;


    public SubstepsCopyAction(final Shell shell, final FailureTrace failureTrace, final Clipboard clipboard) {
        super(JUnitMessages.CopyTrace_action_label);
        Assert.isNotNull(clipboard);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJUnitHelpContextIds.COPYTRACE_ACTION);
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
            if (MessageDialog.openQuestion(shell, JUnitMessages.CopyTraceAction_problem,
                    JUnitMessages.CopyTraceAction_clipboard_busy))
                run();
        }
    }


    public void handleTestSelected(final TestElement test) {
        testElement = test;
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
