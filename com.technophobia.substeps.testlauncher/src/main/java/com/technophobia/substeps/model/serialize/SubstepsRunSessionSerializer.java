package com.technophobia.substeps.model.serialize;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IJavaProject;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.model.structure.FailureTrace;
import com.technophobia.substeps.model.structure.Result;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;
import com.technophobia.substeps.model.structure.SubstepsTestParentElement;
import com.technophobia.substeps.model.structure.SubstepsTestRootElement;

public class SubstepsRunSessionSerializer implements XMLReader {

    private static final String EMPTY = ""; //$NON-NLS-1$
    private static final String CDATA = "CDATA"; //$NON-NLS-1$
    private static final Attributes NO_ATTS = new AttributesImpl();

    private final SubstepsRunSession substepsRunSession;
    private ContentHandler handler;
    private ErrorHandler errorHandler;

    private final NumberFormat timeFormat = new DecimalFormat("0.0##", new DecimalFormatSymbols(Locale.US)); //$NON-NLS-1$ // not localized, parseable by Double.parseDouble(..)


    /**
     * @param substepsRunSession
     *            the test run session to serialize
     */
    public SubstepsRunSessionSerializer(final SubstepsRunSession substepsRunSession) {
        Assert.isNotNull(substepsRunSession);
        this.substepsRunSession = substepsRunSession;
    }


    @Override
    public void parse(final InputSource input) throws IOException, SAXException {
        if (handler == null)
            throw new SAXException("ContentHandler missing"); //$NON-NLS-1$

        handler.startDocument();
        handleTestRun();
        handler.endDocument();
    }


    private void handleTestRun() throws SAXException {
        final AttributesImpl atts = new AttributesImpl();
        addCDATA(atts, IXMLTags.ATTR_NAME, substepsRunSession.getTestRunName());
        final IJavaProject project = substepsRunSession.getLaunchedProject();
        if (project != null)
            addCDATA(atts, IXMLTags.ATTR_PROJECT, project.getElementName());
        addCDATA(atts, IXMLTags.ATTR_TESTS, substepsRunSession.getTotalCount());
        addCDATA(atts, IXMLTags.ATTR_STARTED, substepsRunSession.getStartedCount());
        addCDATA(atts, IXMLTags.ATTR_FAILURES, substepsRunSession.getFailureCount());
        addCDATA(atts, IXMLTags.ATTR_ERRORS, substepsRunSession.getErrorCount());
        addCDATA(atts, IXMLTags.ATTR_IGNORED, substepsRunSession.getIgnoredCount());
        startElement(IXMLTags.NODE_TESTRUN, atts);

        final SubstepsTestRootElement root = substepsRunSession.getTestRoot();
        final SubstepsTestElement[] topSuites = root.getChildren();
        for (int i = 0; i < topSuites.length; i++) {
            handleTestElement(topSuites[i]);
        }

        endElement(IXMLTags.NODE_TESTRUN);
    }


    private void handleTestElement(final SubstepsTestElement testElement) throws SAXException {
        if (testElement instanceof SubstepsTestParentElement) {
            final SubstepsTestParentElement parentElement = (SubstepsTestParentElement) testElement;

            final AttributesImpl atts = new AttributesImpl();
            addCDATA(atts, IXMLTags.ATTR_NAME, parentElement.getTestName());
            if (!Double.isNaN(parentElement.getElapsedTimeInSeconds()))
                addCDATA(atts, IXMLTags.ATTR_TIME, timeFormat.format(parentElement.getElapsedTimeInSeconds()));
            if (!testElement.getStatus().isComplete() || !testElement.getTestResult(false).equals(Result.UNDEFINED))
                addCDATA(atts, IXMLTags.ATTR_INCOMPLETE, Boolean.TRUE.toString());

            startElement(IXMLTags.NODE_TESTSUITE, atts);
            addFailure(testElement);

            final SubstepsTestElement[] children = parentElement.getChildren();
            for (int i = 0; i < children.length; i++) {
                handleTestElement(children[i]);
            }
            endElement(IXMLTags.NODE_TESTSUITE);

        } else if (testElement instanceof SubstepsTestLeafElement) {
            final SubstepsTestLeafElement leafElement = (SubstepsTestLeafElement) testElement;

            final AttributesImpl atts = new AttributesImpl();
            addCDATA(atts, IXMLTags.ATTR_NAME, leafElement.getTestMethodName());
            // addCDATA(atts, IXMLTags.ATTR_CLASSNAME,
            // leafElement.getClassName());
            if (!Double.isNaN(leafElement.getElapsedTimeInSeconds()))
                addCDATA(atts, IXMLTags.ATTR_TIME, timeFormat.format(leafElement.getElapsedTimeInSeconds()));
            if (!testElement.getStatus().isComplete())
                addCDATA(atts, IXMLTags.ATTR_INCOMPLETE, Boolean.TRUE.toString());
            if (leafElement.isIgnored())
                addCDATA(atts, IXMLTags.ATTR_IGNORED, Boolean.TRUE.toString());

            startElement(IXMLTags.NODE_TESTCASE, atts);
            addFailure(testElement);

            endElement(IXMLTags.NODE_TESTCASE);

        } else {
            throw new IllegalStateException(String.valueOf(testElement));
        }

    }


    private void addFailure(final SubstepsTestElement testElement) throws SAXException {
        final FailureTrace failureTrace = testElement.getFailureTrace();
        if (failureTrace != null) {
            final AttributesImpl failureAtts = new AttributesImpl();
            // addCDATA(failureAtts, IXMLTags.ATTR_MESSAGE, xx);
            // addCDATA(failureAtts, IXMLTags.ATTR_TYPE, xx);
            final String failureKind = testElement.getTestResult(false) == Result.ERROR ? IXMLTags.NODE_ERROR
                    : IXMLTags.NODE_FAILURE;
            startElement(failureKind, failureAtts);
            final String expected = failureTrace.getExpected();
            final String actual = failureTrace.getActual();
            if (expected != null) {
                startElement(IXMLTags.NODE_EXPECTED, NO_ATTS);
                addCharacters(expected);
                endElement(IXMLTags.NODE_EXPECTED);
            }
            if (actual != null) {
                startElement(IXMLTags.NODE_ACTUAL, NO_ATTS);
                addCharacters(actual);
                endElement(IXMLTags.NODE_ACTUAL);
            }
            final String trace = failureTrace.getTrace();
            addCharacters(trace);
            endElement(failureKind);
        }
    }


    private void startElement(final String name, final Attributes atts) throws SAXException {
        handler.startElement(EMPTY, name, name, atts);
    }


    private void endElement(final String name) throws SAXException {
        handler.endElement(EMPTY, name, name);
    }


    private static void addCDATA(final AttributesImpl atts, final String name, final int value) {
        addCDATA(atts, name, Integer.toString(value));
    }


    private static void addCDATA(final AttributesImpl atts, final String name, final String value) {
        atts.addAttribute(EMPTY, EMPTY, name, CDATA, value);
    }


    private void addCharacters(String string) throws SAXException {
        string = escapeNonUnicodeChars(string);
        handler.characters(string.toCharArray(), 0, string.length());
    }


    /**
     * Replaces all non-Unicode characters in the given string.
     * 
     * @param string
     *            a string
     * @return string with Java-escapes
     * @since 3.6
     */
    private static String escapeNonUnicodeChars(final String string) {
        StringBuffer buf = null;
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            if (!(ch == 9 || ch == 10 || ch == 13 || ch >= 32)) {
                if (buf == null) {
                    buf = new StringBuffer(string.substring(0, i));
                }
                buf.append("\\u"); //$NON-NLS-1$
                final String hex = Integer.toHexString(ch);
                for (int j = hex.length(); j < 4; j++)
                    buf.append('0');
                buf.append(hex);
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        if (buf != null) {
            return buf.toString();
        }
        return string;
    }


    @Override
    public void setContentHandler(final ContentHandler handler) {
        this.handler = handler;
    }


    @Override
    public ContentHandler getContentHandler() {
        return handler;
    }


    @Override
    public void setErrorHandler(final ErrorHandler handler) {
        errorHandler = handler;
    }


    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }


    // ignored:

    @Override
    public void parse(final String systemId) throws IOException, SAXException {
    }


    @Override
    public void setDTDHandler(final DTDHandler handler) {
    }


    @Override
    public DTDHandler getDTDHandler() {
        return null;
    }


    @Override
    public void setEntityResolver(final EntityResolver resolver) {
    }


    @Override
    public EntityResolver getEntityResolver() {
        return null;
    }


    @Override
    public void setProperty(final java.lang.String name, final java.lang.Object value) {
    }


    @Override
    public Object getProperty(final java.lang.String name) {
        return null;
    }


    @Override
    public void setFeature(final java.lang.String name, final boolean value) {
    }


    @Override
    public boolean getFeature(final java.lang.String name) {
        return false;
    }
}
