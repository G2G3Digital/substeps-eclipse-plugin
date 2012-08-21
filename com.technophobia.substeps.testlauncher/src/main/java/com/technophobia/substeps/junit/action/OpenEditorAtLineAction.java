package com.technophobia.substeps.junit.action;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import com.technophobia.eclipse.ui.NotifyingUiUpdater;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;
import com.technophobia.substeps.supplier.Supplier;

public class OpenEditorAtLineAction extends OpenEditorAction {

    private final int fLineNumber;


    public OpenEditorAtLineAction(final Supplier<SubstepsRunSession> substepsRunSessionSupplier,
            final NotifyingUiUpdater<String> infoMessageUpdater, final Shell shell, final String testClassName,
            final int line) {
        super(substepsRunSessionSupplier, infoMessageUpdater, shell, testClassName);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, SubstepsHelpContextIds.OPENEDITORATLINE_ACTION);
        fLineNumber = line;
    }


    @Override
    protected void reveal(final ITextEditor textEditor) {
        if (fLineNumber >= 0) {
            try {
                final IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
                textEditor.selectAndReveal(document.getLineOffset(fLineNumber - 1),
                        document.getLineLength(fLineNumber - 1));
            } catch (final BadLocationException x) {
                // marker refers to invalid text position -> do nothing
            }
        }
    }


    @Override
    protected IJavaElement findElement(final IJavaProject project, final String testClassName) throws CoreException {
        return findType(project, className);
    }
}
