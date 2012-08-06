package com.technophobia.substeps.document.text.rule;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

public class UntilOtherContentTypeSequenceRule extends MultiLineRule {

	private final Comparator<char[]> lineDelimiterComparator = new DecreasingCharArrayLengthComparator();

	private char[][] lineDelimiters;
	private char[][] sortedLineDelimiters;

	private String[] otherContentTypes;

	public UntilOtherContentTypeSequenceRule(String startSequence,
			IToken token, boolean breaksOnEOF, String... otherContentTypes) {
		super(startSequence, "Not applicable", token, (char)0, breaksOnEOF);
		this.otherContentTypes = otherContentTypes;
	}

	/**
	 * Returns whether the end sequence was detected. As the pattern can be
	 * considered ended by a line delimiter, the result of this method is
	 * <code>true</code> if the rule breaks on the end of the line, or if the
	 * EOF character is read.
	 * 
	 * @param scanner
	 *            the character scanner to be used
	 * @return <code>true</code> if the end sequence has been detected
	 */
	protected boolean endSequenceDetected(ICharacterScanner scanner) {

		char[][] originalDelimiters = scanner.getLegalLineDelimiters();
		int count = originalDelimiters.length;
		if (this.lineDelimiters == null || this.lineDelimiters.length != count) {
			sortedLineDelimiters = new char[count][];
		} else {
			while (count > 0
					&& Arrays.equals(lineDelimiters[count - 1],
							originalDelimiters[count - 1]))
				count--;
		}
		if (count != 0) {
			lineDelimiters = originalDelimiters;
			System.arraycopy(lineDelimiters, 0, sortedLineDelimiters, 0,
					lineDelimiters.length);
			Arrays.sort(sortedLineDelimiters, lineDelimiterComparator);
		}

		int readCount = 1;
		int c;
		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			if (c == fEscapeCharacter) {
				// Skip escaped character(s)
				if (fEscapeContinuesLine) {
					c = scanner.read();
					for (int i = 0; i < sortedLineDelimiters.length; i++) {
						if (c == sortedLineDelimiters[i][0]
								&& sequenceDetected(scanner,
										sortedLineDelimiters[i], fBreaksOnEOF))
							break;
					}
				} else
					scanner.read();

			} else if (isOtherContentTypeFound(scanner, c)) {
				return true;
			} else if (fBreaksOnEOL) {
				// Check for end of line since it can be used to terminate the
				// pattern.
				for (int i = 0; i < sortedLineDelimiters.length; i++) {
					if (c == sortedLineDelimiters[i][0]
							&& sequenceDetected(scanner,
									sortedLineDelimiters[i], fBreaksOnEOF))
						return true;
				}
			}
			readCount++;
		}

		if (fBreaksOnEOF)
			return true;

		for (; readCount > 0; readCount--)
			scanner.unread();

		return false;
	}

	private boolean isOtherContentTypeFound(ICharacterScanner scanner, int c) {
		for (String otherContentType : otherContentTypes) {
			char[] charArr = otherContentType.toCharArray();
			if (otherContentType != null && c == charArr[0]) {

				// Check if the specified end sequence has been found.
				if (sequenceDetected(scanner, charArr, fBreaksOnEOF)) {
					// we've found a match, now unread the other content type
					for(int i=0; i<charArr.length; i++){
						scanner.unread();
					}
					return true;
				}
			}
		}
		return false;
	}

	private static final class DecreasingCharArrayLengthComparator implements
			Comparator<char[]> {
		public int compare(char[] o1, char[] o2) {
			return ((char[]) o2).length - ((char[]) o1).length;
		}
	}
}
