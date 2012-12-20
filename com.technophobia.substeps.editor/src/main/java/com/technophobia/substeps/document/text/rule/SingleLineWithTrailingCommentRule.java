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

import java.util.Arrays;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;


/**
 * Extension of SingleLineRule which handles a line which may have a trailing comment,
 * e.g.:
 * <code>
 *     Given that some condition applies # condition applies
 * </code>
 * Any comment part, i.e. the # and all following text, is ignored when treating the line
 * with this rule. The comment remains in the scanner to be handled by its own rule.
 * <p>
 * A # within any quoted string in the non-comment part of the line is not taken
 * to indicate a trailing comment, so e.g.:
 * <code>
 *     Given that the text contains "something # comment"
 * </code>
 * is not treated as if it had a trailing comment. Single or double quotes may be used,
 * but they must be paired correctly.
 * <p>
 * A single instance of a quote character (" or ') may appear on the line without being escaped,
 * e.g.
 * <code>
 *     Given that I'm using an apostrophe
 * </code>
 * 
 * @author ariley
 *
 */
public class SingleLineWithTrailingCommentRule extends SingleLineRule {
	public static final String TRAILING_COMMENT_START = "#";
	private static final char DEFAULT_ESCAPE_CHAR = '\\';
	private static final int[] QUOTE_CHARS = new int[] {'"', '\''};
	private static final int[] EOL_CHARS = new int[] {'\n', '\r'};
	
	static {
		Arrays.sort(QUOTE_CHARS);
		Arrays.sort(EOL_CHARS);
	}
	
	private char escapeCharacter;
	private boolean escapeContinuesLine;

	/**
	 * Creates a rule for the given starting sequence
	 * which, if detected, will return the specified token.
	 * Any character which follows a backslash character '\'
	 * will be ignored, except for the terminating end of
	 * line character. There is no need for a terminating end
	 * of line character on the last line in a file.
	 *
	 * @param startSequence the pattern's start sequence
	 * @param token the token to be returned on success
	 */
	public SingleLineWithTrailingCommentRule(String startSequence,
			IToken token) {
		this(startSequence, token, DEFAULT_ESCAPE_CHAR);
	}

	/**
	 * Creates a rule for the given starting sequence
	 * which, if detected, will return the specified token.
	 * Any character which follows the given escape character
	 * will be ignored. There is no need for a terminating end
	 * of line character on the last line in a file.
	 *
	 * @param startSequence the pattern's start sequence
	 * @param token the token to be returned on success
	 * @param escapeCharacter the escape character, does not
	 *        continue a line if it appears immediately before the
	 *        end of line character.
	 */
	public SingleLineWithTrailingCommentRule(String startSequence,
			IToken token, char escapeCharacter) {
		this(startSequence, token, escapeCharacter, false);
	}

	/**
	 * Creates a rule for the given starting sequence
	 * which, if detected, will return the specified token.
	 * Any character which follows the given escape character
	 * will be ignored. There is no need for a terminating end
	 * of line character on the last line in a file.
	 *
	 * @param startSequence the pattern's start sequence
	 * @param token the token to be returned on success
	 * @param escapeCharacter the escape character
	 * @param escapeContinuesLine indicates whether the specified escape character is used for line
	 *        continuation, so that an end of line immediately after the escape character does not
	 *        terminate the line, even if <code>breakOnEOL</code> is true
	 */
	public SingleLineWithTrailingCommentRule(String startSequence,
			IToken token, char escapeCharacter, boolean escapeContinuesLine) {
		super(startSequence, TRAILING_COMMENT_START, token, escapeCharacter, true, escapeContinuesLine);	// break on EOF
		this.escapeCharacter = escapeCharacter;
		this.escapeContinuesLine = escapeContinuesLine;
	}

	@Override
	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		final QuoteAwareScanner wrapper = new QuoteAwareScanner(scanner);
		boolean endSequenceDetected = super.endSequenceDetected(wrapper);
		if (endSequenceDetected) {
			// Was it trailing comment or EOL/EOF?
			scanner.unread();
			int c = scanner.read();
			if (c == TRAILING_COMMENT_START.charAt(TRAILING_COMMENT_START.length() - 1)) {
				// Put the comment opener back
				for (int i = 0; i < TRAILING_COMMENT_START.length(); i++) {
					scanner.unread();
				}
			}
		}
		return endSequenceDetected;
	}
	
	/**
	 * Wrapper round a ICharacterScanner which detects and ignores quoted strings within the text.
	 *
	 */
	private class QuoteAwareScanner implements ICharacterScanner {
		private final ICharacterScanner delegate;
		
		private QuoteAwareScanner(ICharacterScanner scanner) {
			this.delegate = scanner;
		}

		@Override
		public char[][] getLegalLineDelimiters() {
			return delegate.getLegalLineDelimiters();
		}

		@Override
		public int getColumn() {
			return delegate.getColumn();
		}

		/**
		 * Get the next character to process.
		 * <p>
		 * If the next character read from the underlying ICharacterScanner is not a quote character
		 * (i.e. " or '), then return it, otherwise continue to read until a matching quote is found,
		 * then return the following character. Quotes must be paired exactly, i.e. a quoted string
		 * which starts with " must be terminated with another ", " will not be matched with '.
		 * </p><p>
		 * Searching stops if EOF is reached, or an unescaped end of line character is read,
		 * or escapeContinuesLine is false and an end of line character is read. In that case,
		 * searching for the closing quote is aborted and all the characters which have
		 * been read are unread. Hence a single quote character without a matching quote
		 * character is treated as a normal character.
		 * </p>
		 */
		@Override
		public int read() {
			// Skip over quoted string 
			int c = delegate.read();
			if (c == escapeCharacter) {
				// Skip the escaped character and return the next character from the scanner
				delegate.read();
			} else if (Arrays.binarySearch(QUOTE_CHARS, c) >= 0) {
				// Keep the char to match with the closing quote
				int openQuote = c;
				// count chars read while searching for complete quoted string, including the opening quote
				int charsRead = 1;
				do {
					c = delegate.read();
					charsRead++;
					if (c == escapeCharacter) {
						// read next char, but don't test for quote, or EOL when escaped EOL continues line
						// so basically ignore it unless it's EOF
						c = delegate.read();
						charsRead++;
					} else if (c == openQuote) {
						// matched opening quote, now continue with the remaining string
						break;
					} else if (escapeContinuesLine && Arrays.binarySearch(EOL_CHARS, c) >= 0) {
						// Escaped EOL continues line
						continue;
					}
					
					if (c == ICharacterScanner.EOF || Arrays.binarySearch(EOL_CHARS, c) >= 0) {
						// EOF or (un-escaped) EOL, quoted string not closed so push all back
						unread(charsRead);
						break;
					}
				} while (true);
			} else {
				// Not a quote, push it back
				delegate.unread();
			}
			return delegate.read();
		}

		@Override
		public void unread() {
			delegate.unread();
		}

		private void unread(int charsRead) {
			for (int i = 0; i < charsRead; i++) {
				delegate.unread();
			}
		}
	}
}
