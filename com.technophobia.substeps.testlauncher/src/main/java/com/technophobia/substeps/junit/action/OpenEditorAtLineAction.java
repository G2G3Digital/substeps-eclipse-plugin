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
