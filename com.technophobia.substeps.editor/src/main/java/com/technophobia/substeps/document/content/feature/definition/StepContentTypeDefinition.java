package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.technophobia.substeps.FeatureEditorPlugin;
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
                projectSupplier(partitionContextSupplier), FeatureEditorPlugin.instance().getSuggestionManager());
        final TextExtractor<ICharacterScanner, IToken> textExtractor = new CharacterScannerToProcessedTextExtractor(
                textProcessor);

        return new SuggestionManagerPredicateRule(token, textExtractor);
    }


    @Override
    public IRule damageRepairerRule(final ColourManager colourManager) {
        return fixedWordSetRule(new String[] { "Given", "When", "Then", "And" },
                colourToken(FeatureColour.PINK, colourManager));
    }


    private Supplier<IProject> projectSupplier(final Supplier<PartitionContext> partitionContextSupplier) {
        return new Supplier<IProject>() {
            @Override
            public IProject get() {
                return partitionContextSupplier.get().currentProject();
            }
        };
    }
}
