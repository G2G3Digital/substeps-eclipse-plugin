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
package com.technophobia.substeps.document.navigation;

import static com.technophobia.substeps.FeatureEditorPlugin.instance;

import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.technophobia.eclipse.transformer.ProjectToJavaProjectTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.editor.FeatureEditor;
import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.Syntax;

/**
 * 
 * @author imoore
 * 
 */
public class JumpToSubStepDefinitionHandler extends AbstractHandler {

    private static final ProjectToJavaProjectTransformer PROJECT_TRANSFORMER = new ProjectToJavaProjectTransformer();


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        final IWorkbenchPage page = window.getActivePage();

        // TextEditor implements IEditorPart
        final IEditorPart activeEditor = page.getActiveEditor();

        final IEditorInput editorInput = activeEditor.getEditorInput();

        final IFile file = (IFile) editorInput.getAdapter(IFile.class);

        final boolean isFeatureFile = file.getFileExtension().toLowerCase().equals("feature");
        final boolean isSubstepsFile = file.getFileExtension().toLowerCase().equals("substeps");

        if (isFeatureFile || isSubstepsFile) {

            final IContainer container = file.getParent();

            final IProject project = container.getProject();

            final FeatureEditor currentEditor = (FeatureEditor) activeEditor.getAdapter(FeatureEditor.class);

            final IDocument currentDocument = currentEditor.getCurrentDocument();

            final IEditorSite editorSite = activeEditor.getEditorSite();

            if (editorSite != null) {
                final ISelectionProvider selectionProvider = editorSite.getSelectionProvider();

                if (selectionProvider != null) {
                    jumpToStepDefinition(page, project, currentDocument, selectionProvider);
                }
            }
        }
        // Must return null, apparently
        return null;
    }


    private void jumpToStepDefinition(final IWorkbenchPage page, final IProject project,
            final IDocument currentDocument, final ISelectionProvider selectionProvider) {
        final int offset = ((ITextSelection) selectionProvider.getSelection()).getOffset();
        // Get the line from where we are
        final String currentLine = getCurrentLine(currentDocument, offset);
        if (currentLine != null) {
            instance().info("F3 lookup on line: " + currentLine);

            // Set the Syntax from SubstepSuggestionProvider
            final Syntax syntax = FeatureEditorPlugin.instance().syntaxFor(project);

            // We can be finding definitions written in either a Substeps file
            // or an annotated method in a Java class, these correspond to a
            // ParentStep or a StepImplementation in the Syntax respectively.
            final ParentStep parentStep = findParent(syntax, currentLine);
            if (parentStep != null) {
                // Open the user defined Substep file.
                OpenSubstepsEditor.open(page,
                PROJECT_TRANSFORMER.from(project), parentStep);
            } else {
                final StepImplementation stepImplementation = findStep(syntax, currentLine);
                if (stepImplementation != null) {
                     OpenJavaEditor.open(PROJECT_TRANSFORMER.from(project),
                     stepImplementation.getMethod());
                }
            }
        }
    }


    private static final ParentStep findParent(final Syntax syntax, final String stepValue) {
        for (final ParentStep rootSubStep : syntax.getSortedRootSubSteps()) {
            if (Pattern.matches(rootSubStep.getParent().getPattern(), stepValue)) {
                return rootSubStep;
            }
        }
        return null;
    }


    private static final StepImplementation findStep(final Syntax syntax, final String stepValue) {
        for (final StepImplementation stepImplementation : syntax.getStepImplementations()) {
            if (Pattern.matches(stepImplementation.getValue(), stepValue)) {
                return stepImplementation;
            }
        }
        return null;
    }


    /**
     * @param currentDocument
     * @param offset
     * @return
     */
    private String getCurrentLine(final IDocument currentDocument, final int offset) {
        String rtn = null;

        int lineNumber;
        try {
            lineNumber = currentDocument.getLineOfOffset(offset);
            final int lineStart = currentDocument.getLineOffset(lineNumber);

            String line = currentDocument.get(lineStart, currentDocument.getLineLength(lineNumber));

            final int commentIdx = line.indexOf("#");

            if (commentIdx >= 0) {
                line = line.substring(0, commentIdx);
            }

            rtn = line.trim();

        } catch (final BadLocationException e) {
            FeatureEditorPlugin.instance().error("BadLocationException getting current line @offset: " + offset, e);
        }

        return rtn;
    }

}
