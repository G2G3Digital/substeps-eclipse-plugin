package com.technophobia.substeps.document.content.feature.definition;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.feature.FeatureColour;
import com.technophobia.substeps.document.text.rule.MultiLineFragmentedSequenceRule;

public abstract class AbstractFeatureContentTypeDefinition implements ContentTypeDefinition {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final String id;
    private final boolean optional;


    public AbstractFeatureContentTypeDefinition(final String id, final boolean optional) {
        super();
        this.id = id;
        this.optional = optional;
    }


    @Override
    public String id() {
        return id;
    }


    @Override
    public boolean isOptional() {
        return optional;
    }


    protected IToken colourToken(final FeatureColour colour, final ColourManager colourManager) {
        return new Token(new TextAttribute(colourManager.getColor(colour.colour())));
    }


    protected IPredicateRule singleLineRule(final String startSequence, final IToken token) {
        return new EndOfLineRule(startSequence, token);
    }


    protected IRule fixedWordRule(final String word, final IToken token) {
        return new WordRule(word(word), token);
    }


    protected IPredicateRule singleLineRule(final String startString, final String tokenId) {
        final IToken token = new Token(tokenId);
        return new EndOfLineRule(startString, token);
    }


    protected IPredicateRule paragraphRule(final String startString, final String tokenId) {
        final IToken token = new Token(tokenId);
        return new MultiLineFragmentedSequenceRule(startString, NEWLINE + NEWLINE, token);
    }


    protected IWordDetector word(final String word) {
        return new IWordDetector() {
            int i = 0;


            @Override
            public boolean isWordStart(final char c) {
                if (word.charAt(0) == c) {
                    i = 0;
                    return true;
                }
                return false;
            }


            @Override
            public boolean isWordPart(final char c) {
                i++;
                return word.length() > i && word.charAt(i) == c;
            }
        };
    }
}
