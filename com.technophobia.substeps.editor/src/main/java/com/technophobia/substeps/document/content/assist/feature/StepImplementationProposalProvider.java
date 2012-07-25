package com.technophobia.substeps.document.content.assist.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.internal.texteditor.spelling.NoCompletionsProposal;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.document.content.assist.CompletionProposalProvider;
import com.technophobia.substeps.document.content.assist.feature.suggestion.StepImplementationSuggestion;
import com.technophobia.substeps.document.content.assist.feature.suggestion.SubstepSuggestion;
import com.technophobia.substeps.document.content.assist.feature.suggestion.Suggestion;
import com.technophobia.substeps.model.ParentStep;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.render.StepImplementationRenderer;
import com.technophobia.substeps.supplier.Transformer;

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
    private final StepImplementationRenderer stepRenderer;
    private final Transformer<IWorkbenchSite, Syntax> siteToSyntaxTransformer;


    public StepImplementationProposalProvider(final IWorkbenchPartSite site,
            final StepImplementationRenderer stepRenderer,
            final Transformer<IWorkbenchSite, Syntax> siteToSyntaxTransformer) {
        this.site = site;
        this.stepRenderer = stepRenderer;
        this.siteToSyntaxTransformer = siteToSyntaxTransformer;
    }


    @Override
    public ICompletionProposal[] get(final IDocument document, final int offset) {
        final Syntax syntax = siteToSyntaxTransformer.to(site);
        final Collection<Suggestion> sortedSuggestions = getSuggestionsForSyntax(syntax);
        return createCompletionsForSuggestions(document, offset, sortedSuggestions);
    }


    /**
     * Convert {@link Syntax} into its {@link StepImplementation} and
     * {@link ParentStep} substeps, and sort
     * 
     * @param syntax
     *            The syntax provided
     * @return sorted list of suggestions
     */
    private List<Suggestion> getSuggestionsForSyntax(final Syntax syntax) {
        final List<Suggestion> suggestions = new ArrayList<Suggestion>();
        for (final StepImplementation step : syntax.getStepImplementations()) {
            suggestions.add(new StepImplementationSuggestion(stepRenderer.render(step)));
        }

        for (final ParentStep substep : syntax.getSortedRootSubSteps()) {
            suggestions.add(new SubstepSuggestion(substep.getParent().getLine()));
        }

        Collections.sort(suggestions);
        return suggestions;
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
        final String startOfLastWord = getLastWord(document, offset);

        for (final Suggestion suggestion : suggestions) {
            final String suggestionStr = suggestion.getSuggestionString();
            // TODO - position the cursor at the first < in order to be able
            // to replace with something sensible
            if (startOfLastWord == null) {
                completionProposals.add(new CompletionProposal(suggestionStr, offset, 0, suggestionStr.length()));
            } else {
                // only include if the suggestion matches
                if (suggestionStr.toUpperCase().startsWith(startOfLastWord.toUpperCase())) {
                    // String actualReplacement =
                    // replacement.substring(startOfLastWord.length());
                    // result.add(
                    // new CompletionProposal(actualReplacement, offset, 0,
                    // actualReplacement.length()));

                    completionProposals.add(new CompletionProposal(suggestionStr, offset - startOfLastWord.length(),
                            startOfLastWord.length(), suggestionStr.length()));

                }
            }
        }
        return completionProposals;
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
    private String getLastWord(final IDocument document, final int offset) {
        try {
            final int lineNumber = document.getLineOfOffset(offset);
            final int lineStart = document.getLineOffset(lineNumber);

            final String line = document.get(lineStart, offset - lineStart);
            return line.trim();

        } catch (final BadLocationException e) {
            FeatureEditorPlugin.log(IStatus.ERROR, "Could not get last word");
            // ... log the exception ...
        }
        return null;
    }

}
