package com.technophobia.substeps.document.content.feature.partiton;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class SuggestionManagerPredicateRule implements IPredicateRule {

    private final IToken token;
    private final TextExtractor<ICharacterScanner, IToken> textExtractor;


    public SuggestionManagerPredicateRule(final IToken token,
            final TextExtractor<ICharacterScanner, IToken> textExtractor) {
        this.token = token;
        this.textExtractor = textExtractor;
    }


    @Override
    public IToken getSuccessToken() {
        return token;
    }


    @Override
    public IToken evaluate(final ICharacterScanner scanner) {
        return evaluate(scanner, false);
    }


    @Override
    public IToken evaluate(final ICharacterScanner scanner, final boolean resume) {
        final IToken resToken = textExtractor.extractText(scanner);

        return resToken != null ? resToken : Token.UNDEFINED;
    }
}
