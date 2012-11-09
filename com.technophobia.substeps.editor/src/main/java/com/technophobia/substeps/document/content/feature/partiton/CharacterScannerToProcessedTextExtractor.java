package com.technophobia.substeps.document.content.feature.partiton;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;

public class CharacterScannerToProcessedTextExtractor implements TextExtractor<ICharacterScanner, IToken> {

    private static final int EOF = -1;
    private static final int NEWLINE = '\n';
    private static final int COMMENT = '#';

    private final TextProcessor<IToken> textProcessor;


    public CharacterScannerToProcessedTextExtractor(final TextProcessor<IToken> textProcessor) {
        this.textProcessor = textProcessor;
    }


    @Override
    public IToken extractText(final ICharacterScanner scanner) {
        final String text = readLine(scanner);
        final IToken token = textProcessor.doWithText(text.trim());

        // Didn't find it, so unread everything
        if (token == null) {
            unread(scanner, text.length());
        }

        return token;
    }


    private String readLine(final ICharacterScanner scanner) {
        final StringBuilder sb = new StringBuilder();
        int c = scanner.read();
        while (c != EOF && c != NEWLINE && c != COMMENT) {
            sb.append((char) c);
            c = scanner.read();
        }

        final String text = sb.toString();
        return text;
    }


    private void unread(final ICharacterScanner scanner, final int length) {
        for (int i = 0; i < length; i++) {
            scanner.unread();
        }
    }

}
