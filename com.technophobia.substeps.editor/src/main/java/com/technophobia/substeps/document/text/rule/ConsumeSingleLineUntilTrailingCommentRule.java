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
