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
package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.content.feature.partiton.CharacterScannerToProcessedTextExtractor;
import com.technophobia.substeps.document.content.feature.partiton.SuggestionManagerPredicateRule;
import com.technophobia.substeps.document.content.feature.partiton.SuggestionManagerTextProcessor;
import com.technophobia.substeps.document.content.feature.partiton.TextExtractor;
import com.technophobia.substeps.document.content.feature.partiton.TextProcessor;
import com.technophobia.substeps.document.formatting.FormattingContext;
import com.technophobia.substeps.document.formatting.strategy.FixedIndentFormattingStrategy;
import com.technophobia.substeps.document.partition.PartitionContext;
import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.supplier.Supplier;

public class StepContentTypeDefinition extends AbstractFeatureContentTypeDefinition {

    public static final String CONTENT_TYPE_ID = "__feature_step";


    public StepContentTypeDefinition() {
        super(CONTENT_TYPE_ID, "Step", false);
    }


    @Override
    public IFormattingStrategy formattingStrategy(final Supplier<FormattingContext> formattingContextSupplier) {
        return new FixedIndentFormattingStrategy("\t", formattingContextSupplier);
    }


    @Override
    public IPredicateRule partitionRule(final Supplier<PartitionContext> partitionContextSupplier) {

        final Token token = new Token(id());
        final TextProcessor<IToken> textProcessor = new SuggestionManagerTextProcessor(token,
                projectSupplier(partitionContextSupplier), suggestionManagerSupplier(partitionContextSupplier));
        final TextExtractor<ICharacterScanner, IToken> textExtractor = new CharacterScannerToProcessedTextExtractor(
                textProcessor);

        return new SuggestionManagerPredicateRule(token, textExtractor);
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager,
            final Supplier<PartitionContext> partitionContextSupplier) {
        final IToken token = colourToken(FeatureColour.BLUE, colourManager);
        final TextProcessor<IToken> textProcessor = new SuggestionManagerTextProcessor(token,
                projectSupplier(partitionContextSupplier), suggestionManagerSupplier(partitionContextSupplier));
        final TextExtractor<ICharacterScanner, IToken> textExtractor = new CharacterScannerToProcessedTextExtractor(
                textProcessor);

        return new SuggestionManagerPredicateRule(token, textExtractor);
    }


    private Supplier<IProject> projectSupplier(final Supplier<PartitionContext> partitionContextSupplier) {
        return new Supplier<IProject>() {
            @Override
            public IProject get() {
                return partitionContextSupplier.get().currentProject();
            }
        };
    }


    private Supplier<ContextualSuggestionManager> suggestionManagerSupplier(
            final Supplier<PartitionContext> partitionContextSupplier) {
        return new Supplier<ContextualSuggestionManager>() {
            @Override
            public ContextualSuggestionManager get() {
                return partitionContextSupplier.get().suggestionManager();
            }
        };
    }
}
