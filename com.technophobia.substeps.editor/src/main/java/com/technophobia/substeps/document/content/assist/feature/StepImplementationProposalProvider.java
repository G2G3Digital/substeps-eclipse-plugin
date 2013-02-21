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
package com.technophobia.substeps.document.content.assist.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.document.content.assist.CompletionProposalProvider;
import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.Suggestion;

/**
 * Implementation of {@link CompletionProposalProvider} that looks up all class
 * files in the current {@link IJavaProject}, locates their step definition
 * methods (if any), and filters out non-applicable ones, returning the
 * remainder as {@link ICompletionProposal}s
 * 
 * @author sforbes
 * 
 */
public class StepImplementationProposalProvider implements CompletionProposalProvider {

    private final IWorkbenchPartSite site;
    private final ContextualSuggestionManager suggestionManager;


    public StepImplementationProposalProvider(final IWorkbenchPartSite site,
            final ContextualSuggestionManager suggestionManager) {
        this.site = site;
        this.suggestionManager = suggestionManager;
    }


    @Override
    public ICompletionProposal[] get(final IDocument document, final int offset) {
        // final Syntax syntax = siteToSyntaxTransformer.to(site);
        final IResource resource = activeEditorResource();

        final List<Suggestion> suggestions = suggestionManager.suggestionsFor(resource);
        Collections.sort(suggestions);

        return createCompletionsForSuggestions(document, offset, suggestions);
    }


    /**
     * Find the current resource for the active editor
     * 
     * @return
     */
    private IResource activeEditorResource() {
        return (IResource) site.getWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput()
                .getAdapter(IResource.class);
    }


    /**
     * Filter out non-applicable steps, based on the current position of the
     * cursor in the document. Convert the remainder to
     * {@link ICompletionProposal}
     * 
     * @param document
     *            The currently edited document
     * @param offset
     *            position in the document of the cursor
     * @param suggestions
     *            All suggestions
     * @return Applicable {@link ICompletionProposal}s
     */
    private ICompletionProposal[] createCompletionsForSuggestions(final IDocument document, final int offset,
            final Collection<Suggestion> suggestions) {

        final Collection<ICompletionProposal> completionProposals = createPopulatedCompletionsForSuggestions(document,
                offset, suggestions);

        if (completionProposals.isEmpty()) {
            completionProposals.add(new NoCompletionsProposal());
        }

        return completionProposals.toArray(new ICompletionProposal[completionProposals.size()]);
    }


    private Collection<ICompletionProposal> createPopulatedCompletionsForSuggestions(final IDocument document,
            final int offset, final Collection<Suggestion> suggestions) {
        final List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();

        // filter the list based on current text
        final String startOfLine = getLineUpTo(document, offset);

        final Set<Suggestion> existingSuggestions = new HashSet<Suggestion>();

        for (final Suggestion suggestion : suggestions) {
            final String suggestionText = suggestion.getText();
            if (!isExistingSuggestion(suggestionText, existingSuggestions)) {
                if (startOfLine == null) {
                    completionProposals.add(new CompletionProposal(suggestionText, offset, 0, suggestionText.length()));
                } else {
                    // only include if the suggestion matches
                    if (suggestion.isMatch(startOfLine)) {
                        completionProposals.add(new CompletionProposal(suggestionText, offset - startOfLine.length(),
                                startOfLine.length(), suggestionText.length()));
                    } else if (suggestion.isPartialMatch(startOfLine)) {
                        // TODO: Rich wanted to look at this, by doing something
                        // funky with partial patterns or something
                        completionProposals.add(new CompletionProposal(suggestionText, offset - startOfLine.length(),
                                startOfLine.length(), suggestionText.length()));
                    }
                }
                existingSuggestions.add(suggestion);
            }
        }
        return completionProposals;
    }


    private boolean isExistingSuggestion(final String suggestionText, final Set<Suggestion> existingSuggestions) {
        for (final Suggestion suggestion : existingSuggestions) {
            if (suggestion.isMatch(suggestionText)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the current line, up to the point offset
     * 
     * @param document
     *            The currently edited document
     * @param offset
     *            position in the document of the cursor
     * @return
     */
    private String getLineUpTo(final IDocument document, final int offset) {
        try {
            final int lineNumber = document.getLineOfOffset(offset);
            final int lineStart = document.getLineOffset(lineNumber);

            final String line = document.get(lineStart, offset - lineStart);
            return removeLeadingSpace(line);

        } catch (final BadLocationException e) {
            FeatureEditorPlugin.instance().error("Could not get last word");
            // ... log the exception ...
        }
        return null;
    }


    private String removeLeadingSpace(final String text) {
        return text.replaceAll("^\\s+", "");
    }
}
