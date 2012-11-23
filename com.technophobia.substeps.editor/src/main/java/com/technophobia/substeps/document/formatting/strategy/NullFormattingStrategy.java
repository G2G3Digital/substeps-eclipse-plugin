package com.technophobia.substeps.document.formatting.strategy;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

/**
 * Null Object pattern for {@link IFormattingStrategy} interface
 * 
 * @author sforbes
 * 
 */
public class NullFormattingStrategy implements IFormattingStrategy {

    protected static final String NEWLINE = System.getProperty("line.separator");


    public NullFormattingStrategy() {
        super();
    }


    @Override
    public void formatterStarts(final String initialIndentation) {
        // no-op
    }


    @Override
    public void formatterStops() {
        // no-op
    }


    @Override
    public String format(final String content, final boolean isLineStart, final String indentation,
            final int[] positions) {
        return "";
        /*
         * final StringBuffer sb = new StringBuffer(content.replaceAll(" ",
         * "").replaceAll("\t", "")); int numNewLines = 0;
         * 
         * for (int i = sb.length() - 1; i >= 0 && isNewline(sb, i); i -=
         * NEWLINE.length()) { if (numNewLines >= 2) { sb.deleteCharAt(i); }
         * else { numNewLines++; } }
         * 
         * return sb.toString();
         */
    }


    private boolean isNewline(final StringBuffer sb, final int endPos) {
        return sb.substring(endPos - (NEWLINE.length() - 1), endPos + 1).equals(NEWLINE);
    }
}
