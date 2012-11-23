package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

/**
 * Default implementation of {@link IFormattingStrategy} that simply returns the
 * content as-is
 * 
 * @author sforbes
 * 
 */
public class DefaultFormattingStrategy implements IFormattingStrategy {

    public static final String NEWLINE = System.getProperty("line.separator");


    public DefaultFormattingStrategy() {
        super();
    }


    @Override
    public void formatterStarts(final String initialIndentation) {
        // no-op
    }


    @Override
    public String format(final String content, final boolean isLineStart, final String indentation,
            final int[] positions) {
        return content;
    }


    @Override
    public void formatterStops() {
        // no-op
    }

}
