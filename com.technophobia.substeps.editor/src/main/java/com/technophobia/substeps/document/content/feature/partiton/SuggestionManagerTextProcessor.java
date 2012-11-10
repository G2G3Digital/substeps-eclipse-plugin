package com.technophobia.substeps.document.content.feature.partiton;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.rules.IToken;

import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.SuggestionType;
import com.technophobia.substeps.supplier.Supplier;

public class SuggestionManagerTextProcessor implements TextProcessor<IToken> {

    private final Supplier<IProject> projectSupplier;
    private final Supplier<ContextualSuggestionManager> suggestionManagerSupplier;
    private final IToken successToken;


    public SuggestionManagerTextProcessor(final IToken successToken, final Supplier<IProject> projectSupplier,
            final Supplier<ContextualSuggestionManager> suggestionManagerSupplier) {
        this.successToken = successToken;
        this.projectSupplier = projectSupplier;
        this.suggestionManagerSupplier = suggestionManagerSupplier;
    }


    @Override
    public IToken doWithText(final String text) {

        final IProject project = projectSupplier.get();
        if (project != null) {

            final Collection<String> suggestions = new ArrayList<String>(suggestionManagerSupplier.get()
                    .suggestionsFor(SuggestionType.FEATURE, project));

            final boolean found = findSuggestion(text.trim(), suggestions);
            if (found) {
                return successToken;
            }
        }
        return null;
    }


    private boolean findSuggestion(final String text, final Collection<String> suggestions) {
        for (final String suggestion : suggestions) {
            if (text.equals(suggestion)) {
                return true;
            }
        }
        return false;
    }

}
