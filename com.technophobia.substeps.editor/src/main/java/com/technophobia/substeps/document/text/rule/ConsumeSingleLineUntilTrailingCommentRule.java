package com.technophobia.substeps.document.text.rule;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;

public class ConsumeSingleLineUntilTrailingCommentRule implements IRule {
    private static final int EOF = -1;
    private static final int NEWLINE = '\n';
    private static final int COMMENT = '#';

    private final IToken token;


    public ConsumeSingleLineUntilTrailingCommentRule(final IToken token) {
        this.token = token;
    }


    @Override
    public IToken evaluate(final ICharacterScanner scanner) {
        int c = scanner.read();
        while (c != EOF && c != NEWLINE && c != COMMENT) {
            c = scanner.read();
        }
        return token;
    }

}
