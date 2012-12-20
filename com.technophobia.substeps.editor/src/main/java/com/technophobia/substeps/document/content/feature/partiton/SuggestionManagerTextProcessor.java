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
package com.technophobia.substeps.document.content.feature.partiton;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.rules.IToken;

import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.Suggestion;
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

            final Collection<Suggestion> suggestions = new ArrayList<Suggestion>(suggestionManagerSupplier.get()
                    .suggestionsFor(project));

            final boolean found = findSuggestion(text.trim(), suggestions);
            if (found) {
                return successToken;
            }
        }
        return null;
    }


    private boolean findSuggestion(final String text, final Collection<Suggestion> suggestions) {
        for (final Suggestion suggestion : suggestions) {
            if (suggestion.isMatch(text)) {
                return true;
            }
        }
        return false;
    }

}
