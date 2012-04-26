package com.technophobia.substeps.document.formatting;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;

@RunWith(JMock.class)
public class PartitionedFormattingContextTest {

	private Mockery context;

	private IDocument document;
	private IRegion region;
	private ContentTypeDefinitionFactory contentTypeDefinitionFactory;

	private FormattingContext formattingContext;

	@Before
	public void initialise() {
		this.context = new Mockery();

		this.document = context.mock(IDocument.class);
		this.region = context.mock(IRegion.class, "currentRegion");
		this.contentTypeDefinitionFactory = context
				.mock(ContentTypeDefinitionFactory.class);

		this.formattingContext = new PartitionedFormattingContext(document,
				region, contentTypeDefinitionFactory);
	}

	@Test
	public void hasPreviousLineReturnsFalseForFirstLine() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(0));

				oneOf(document).getLineOfOffset(0);
				will(returnValue(0));
			}
		});

		assertFalse(formattingContext.hasPreviousLine());
	}

	@Test
	public void hasPreviousLineReturnsTrueForSecondLine() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(60));

				oneOf(document).getLineOfOffset(60);
				will(returnValue(1));
			}
		});

		assertTrue(formattingContext.hasPreviousLine());
	}

	@Test
	public void previousLineThrowsExceptionForFirstLine() throws Exception {

		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(0));

				oneOf(document).getLineOfOffset(0);
				will(returnValue(0));
			}
		});
		try {
			formattingContext.previousLine();
			fail("Expected " + InvalidFormatPositionException.class.getName()
					+ " to be thrown");
		} catch (final InvalidFormatPositionException ex) {
		}
	}

	@Test
	public void previousLineReturnsCorrectLine() throws Exception {
		final String expectedResult = "This is line 2";

		prepareDocument(80, expectedResult.length(), 2, 1, expectedResult);

		assertThat(formattingContext.previousLine(), is("This is line 2"));
	}

	@Test
	public void hasNextLineReturnsFalseForLastLine() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(60));

				oneOf(document).getLineOfOffset(60);
				will(returnValue(1));

				oneOf(document).getNumberOfLines();
				will(returnValue(2));
			}
		});

		assertFalse(formattingContext.hasNextLine());
	}

	@Test
	public void hasNextLineReturnsTrueForFirstLine() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(0));

				oneOf(document).getLineOfOffset(0);
				will(returnValue(0));

				oneOf(document).getNumberOfLines();
				will(returnValue(2));
			}
		});

		assertTrue(formattingContext.hasNextLine());
	}

	@Test
	public void nextLineReturnsThrowsExceptionForLastLine() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(60));

				oneOf(document).getLineOfOffset(60);
				will(returnValue(1));

				oneOf(document).getNumberOfLines();
				will(returnValue(2));
			}
		});
		try {
			formattingContext.nextLine();
			fail("Expected " + InvalidFormatPositionException.class.getName()
					+ " to be thrown");
		} catch (final InvalidFormatPositionException ex) {
		}
	}

	@Test
	public void nextLineReturnsCorrectLine() throws Exception {
		final String expectedResult = "This is line 3";

		prepareDocument(120, expectedResult.length(), 1, 2, 3, expectedResult);

		assertThat(formattingContext.nextLine(), is("This is line 3"));
	}

	@Test
	public void hasPreviousContentTypeReturnsFalseForFirstLine()
			throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(0));
			}
		});

		assertFalse(formattingContext.hasPreviousContentType());
	}

	@Test
	public void hasPreviousContentTypeReturnsTrueForSecondLine()
			throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(60));
			}
		});

		assertTrue(formattingContext.hasPreviousContentType());
	}

	@Test
	public void previousContentTypeThrowsExceptionForFirstContentType()
			throws BadLocationException {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(0));
			}
		});
		try {
			formattingContext.previousContentType();
			fail("Expected " + InvalidFormatPositionException.class.getName()
					+ " to be thrown");
		} catch (final InvalidFormatPositionException ex) {
		}
	}

	@Test
	public void previousContentTypeReturnsCorrectContentType() throws Exception {
		final ContentTypeDefinition previousContentType = context
				.mock(ContentTypeDefinition.class);

		context.checking(new Expectations() {
			{
				exactly(2).of(region).getOffset();
				will(returnValue(31));

				oneOf(document).getContentType(30);
				will(returnValue("previousContentType"));

				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"previousContentType");
				will(returnValue(previousContentType));
			}
		});

		assertThat(formattingContext.previousContentType(),
				is(previousContentType));
	}

	@Test
	public void hasNextContentTypeReturnsFalseForLastLine() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(30));

				oneOf(region).getLength();
				will(returnValue(10));

				oneOf(document).getLength();
				will(returnValue(40));
			}
		});

		assertFalse(formattingContext.hasNextContentType());
	}

	@Test
	public void hasNextContentTypeReturnsTrueForFirstLine() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(0));

				oneOf(region).getLength();
				will(returnValue(10));

				oneOf(document).getLength();
				will(returnValue(30));
			}
		});

		assertTrue(formattingContext.hasNextContentType());
	}

	@Test
	public void nextContentTypeThrowsExceptionForLastContentType()
			throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(120));

				oneOf(region).getLength();
				will(returnValue(20));

				oneOf(document).getLength();
				will(returnValue(140));
			}
		});
		try {
			formattingContext.nextContentType();
			fail("Expected " + InvalidFormatPositionException.class.getName()
					+ " to be thrown");
		} catch (final InvalidFormatPositionException ex) {
		}
	}

	@Test
	public void nextContentTypeReturnsCorrectContentType() throws Exception {
		final ContentTypeDefinition nextContentType = context
				.mock(ContentTypeDefinition.class);

		context.checking(new Expectations() {
			{
				exactly(2).of(region).getOffset();
				will(returnValue(0));

				exactly(2).of(region).getLength();
				will(returnValue(30));

				oneOf(document).getContentType(31);
				will(returnValue("nextContentType"));

				oneOf(document).getLength();
				will(returnValue(100));

				oneOf(contentTypeDefinitionFactory).contentTypeDefintionByName(
						"nextContentType");
				will(returnValue(nextContentType));
			}
		});

		assertThat(formattingContext.nextContentType(), is(nextContentType));
	}

	private void prepareDocument(final int newRegionOffset,
			final int newRegionLength, final int currentLine,
			final int newLine, final String expectedResult) throws Exception {
		prepareDocument(newRegionOffset, newRegionLength, currentLine, newLine,
				Integer.MAX_VALUE, expectedResult);
	}

	private void prepareDocument(final int newRegionOffset,
			final int newRegionLength, final int currentLine,
			final int newLine, final int numLines, final String expectedResult)
			throws Exception {

		final IRegion newLineRegion = prepareRegionFor(newRegionOffset,
				newRegionLength);

		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(123));

				oneOf(document).getLineOfOffset(123);
				will(returnValue(currentLine));

				oneOf(document).get(newRegionOffset, newRegionLength);
				will(returnValue(expectedResult));

				oneOf(document).getLineInformation(newLine);
				will(returnValue(newLineRegion));

				between(0, 1).of(document).getNumberOfLines();
				will(returnValue(numLines));
			}
		});
	}

	private IRegion prepareRegionFor(final int offset, final int length) {
		final IRegion region = context.mock(IRegion.class, "newRegion");
		context.checking(new Expectations() {
			{
				oneOf(region).getOffset();
				will(returnValue(offset));

				oneOf(region).getLength();
				will(returnValue(length));
			}
		});
		return region;
	}
}
