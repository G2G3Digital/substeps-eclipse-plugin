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
package com.technophobia.substeps.model;

import org.xml.sax.helpers.DefaultHandler;

public class SubstepsRunHandler extends DefaultHandler {

    /*
     * TODO: validate (currently assumes correct XML)
     */

    /*
     * private int id;
     * 
     * private SubstepsRunSession substepsRunSession; private
     * SubstepsTestParentElement rootElement; private SubstepsTestLeafElement
     * leafElement; private final Stack<Boolean> notRun = new Stack<Boolean>();
     * 
     * private StringBuffer failureBuffer; private boolean inExpected; private
     * boolean inActual; private StringBuffer expectedBuffer; private
     * StringBuffer actualBuffer;
     * 
     * private Locator locator;
     * 
     * private Status status;
     * 
     * private IProgressMonitor monitor; private int lastReportedLine;
     * 
     * 
     * public SubstepsRunHandler() {
     * 
     * }
     * 
     * 
     * public SubstepsRunHandler(final IProgressMonitor monitor) { this.monitor
     * = monitor; }
     * 
     * 
     * public SubstepsRunHandler(final SubstepsRunSession substepsRunSession) {
     * this.substepsRunSession = substepsRunSession; }
     * 
     * 
     * @Override public void setDocumentLocator(final Locator locator) {
     * this.locator = locator; }
     * 
     * 
     * @Override public void startDocument() throws SAXException { }
     * 
     * 
     * @Override public void startElement(final String uri, final String
     * localName, final String qName, final Attributes attributes) throws
     * SAXException { if (locator != null && monitor != null) { int line =
     * locator.getLineNumber(); if (line - 20 >= lastReportedLine) { line -=
     * line % 20; lastReportedLine = line;
     * monitor.subTask(NLS.bind(SubstepsFeatureMessages
     * .SubstepsRunHandler_lines_read, new Integer(line))); } } if
     * (Thread.interrupted()) throw new OperationCanceledException();
     * 
     * if (qName.equals(IXMLTags.NODE_TESTRUN)) { if (substepsRunSession ==
     * null) { final String name = attributes.getValue(IXMLTags.ATTR_NAME);
     * final String project = attributes.getValue(IXMLTags.ATTR_PROJECT);
     * IJavaProject javaProject = null; if (project != null) { final IJavaModel
     * javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
     * javaProject = javaModel.getJavaProject(project); if
     * (!javaProject.exists()) javaProject = null; } substepsRunSession = new
     * SubstepsRunSessionImpl(name, javaProject); // TODO: read counts?
     * 
     * } else { substepsRunSession.reset(); } rootElement =
     * substepsRunSession.getTestRoot();
     * 
     * } else if (qName.equals(IXMLTags.NODE_TESTSUITES)) { // support Ant's
     * 'junitreport' task; create suite from // NODE_TESTSUITE
     * 
     * } else if (qName.equals(IXMLTags.NODE_TESTSUITE)) { final String name =
     * attributes.getValue(IXMLTags.ATTR_NAME);
     * 
     * if (substepsRunSession == null) { // support standalone suites and Ant's
     * 'junitreport' task: substepsRunSession = new SubstepsRunSessionImpl(name,
     * null); rootElement = substepsRunSession.getTestRoot(); }
     * 
     * final String pack = attributes.getValue(IXMLTags.ATTR_PACKAGE); final
     * String suiteName = pack == null ? name : pack + "." + name; //$NON-NLS-1$
     * rootElement = substepsRunSession.createTestElement(rootElement,
     * getNextId(), suiteName, true, 0); readTime(rootElement, attributes);
     * notRun
     * .push(Boolean.valueOf(attributes.getValue(IXMLTags.ATTR_INCOMPLETE)));
     * 
     * } else if (qName.equals(IXMLTags.NODE_PROPERTIES) ||
     * qName.equals(IXMLTags.NODE_PROPERTY)) { // not interested
     * 
     * } else if (qName.equals(IXMLTags.NODE_TESTCASE)) { final String name =
     * attributes.getValue(IXMLTags.ATTR_NAME); final String classname =
     * attributes.getValue(IXMLTags.ATTR_CLASSNAME); leafElement =
     * (SubstepsTestLeafElement)
     * substepsRunSession.createTestElement(rootElement, getNextId(), name + '('
     * + classname + ')', false, 0);
     * notRun.push(Boolean.valueOf(attributes.getValue
     * (IXMLTags.ATTR_INCOMPLETE)));
     * leafElement.setIgnored(Boolean.valueOf(attributes
     * .getValue(IXMLTags.ATTR_IGNORED)).booleanValue()); readTime(testCase,
     * attributes);
     * 
     * } else if (qName.equals(IXMLTags.NODE_ERROR)) { // TODO: multiple
     * failures: // https://bugs.eclipse.org/bugs/show_bug.cgi?id=125296 status
     * = Status.ERROR; failureBuffer = new StringBuffer();
     * 
     * } else if (qName.equals(IXMLTags.NODE_FAILURE)) { // TODO: multiple
     * failures: // https://bugs.eclipse.org/bugs/show_bug.cgi?id=125296 status
     * = Status.FAILURE; failureBuffer = new StringBuffer();
     * 
     * } else if (qName.equals(IXMLTags.NODE_EXPECTED)) { inExpected = true;
     * expectedBuffer = new StringBuffer();
     * 
     * } else if (qName.equals(IXMLTags.NODE_ACTUAL)) { inActual = true;
     * actualBuffer = new StringBuffer();
     * 
     * } else if (qName.equals(IXMLTags.NODE_SYSTEM_OUT) ||
     * qName.equals(IXMLTags.NODE_SYSTEM_ERR)) { // not interested
     * 
     * } else if (qName.equals(IXMLTags.NODE_SKIPPED)) { // not an Ant JUnit
     * tag, see // https://bugs.eclipse.org/bugs/show_bug.cgi?id=276068
     * leafElement.setIgnored(true);
     * 
     * } else { throw new SAXParseException("unknown node '" + qName + "'",
     * locator); //$NON-NLS-1$//$NON-NLS-2$ } }
     * 
     * 
     * private void readTime(final TestElement testElement, final Attributes
     * attributes) { final String timeString =
     * attributes.getValue(IXMLTags.ATTR_TIME); if (timeString != null) { try {
     * testElement.setElapsedTimeInSeconds(Double.parseDouble(timeString)); }
     * catch (final NumberFormatException e) { } } }
     * 
     * 
     * @Override public void characters(final char[] ch, final int start, final
     * int length) throws SAXException { if (inExpected) {
     * expectedBuffer.append(ch, start, length);
     * 
     * } else if (inActual) { actualBuffer.append(ch, start, length);
     * 
     * } else if (failureBuffer != null) { failureBuffer.append(ch, start,
     * length); } }
     * 
     * 
     * @Override public void endElement(final String uri, final String
     * localName, final String qName) throws SAXException { if
     * (qName.equals(IXMLTags.NODE_TESTRUN)) { // OK
     * 
     * } else if (qName.equals(IXMLTags.NODE_TESTSUITES)) { // OK
     * 
     * } else if (qName.equals(IXMLTags.NODE_TESTSUITE)) {
     * handleTestElementEnd(rootElement); rootElement = rootElement.getParent();
     * // TODO: end suite: compare counters?
     * 
     * } else if (qName.equals(IXMLTags.NODE_PROPERTIES) ||
     * qName.equals(IXMLTags.NODE_PROPERTY)) { // OK
     * 
     * } else if (qName.equals(IXMLTags.NODE_TESTCASE)) {
     * handleTestElementEnd(leafElement); leafElement = null;
     * 
     * } else if (qName.equals(IXMLTags.NODE_FAILURE) ||
     * qName.equals(IXMLTags.NODE_ERROR)) { SubstepsTestElement testElement =
     * leafElement; if (testElement == null) testElement = rootElement;
     * handleFailure(testElement);
     * 
     * } else if (qName.equals(IXMLTags.NODE_EXPECTED)) { inExpected = false; if
     * (failureBuffer != null) { // skip whitespace from before <expected> and
     * <actual> nodes failureBuffer.setLength(0); }
     * 
     * } else if (qName.equals(IXMLTags.NODE_ACTUAL)) { inActual = false; if
     * (failureBuffer != null) { // skip whitespace from before <expected> and
     * <actual> nodes failureBuffer.setLength(0); }
     * 
     * } else if (qName.equals(IXMLTags.NODE_SYSTEM_OUT) ||
     * qName.equals(IXMLTags.NODE_SYSTEM_ERR)) { // OK
     * 
     * } else if (qName.equals(IXMLTags.NODE_SKIPPED)) { // OK
     * 
     * } else {
     * 
     * handleUnknownNode(qName); } }
     * 
     * 
     * private void handleTestElementEnd(final SubstepsTestElement testElement)
     * { final boolean completed = notRun.pop() != Boolean.TRUE;
     * substepsRunSession.registerTestEnded(testElement, completed); }
     * 
     * 
     * private void handleFailure(final SubstepsTestElement testElement) { if
     * (failureBuffer != null) {
     * substepsRunSession.registerTestFailureStatus(testElement, status,
     * failureBuffer.toString(), toString(expectedBuffer),
     * toString(actualBuffer)); failureBuffer = null; expectedBuffer = null;
     * actualBuffer = null; status = null; } }
     * 
     * 
     * private String toString(final StringBuffer buffer) { return buffer !=
     * null ? buffer.toString() : null; }
     * 
     * 
     * private void handleUnknownNode(final String qName) throws SAXException {
     * // TODO: just log if debug option is enabled? String msg =
     * "unknown node '" + qName + "'"; //$NON-NLS-1$//$NON-NLS-2$ if (locator !=
     * null) { msg += " at line " + locator.getLineNumber() + ", column " +
     * locator.getColumnNumber(); //$NON-NLS-1$//$NON-NLS-2$ } throw new
     * SAXException(msg); }
     * 
     * 
     * @Override public void error(final SAXParseException e) throws
     * SAXException { throw e; }
     * 
     * 
     * @Override public void warning(final SAXParseException e) throws
     * SAXException { throw e; }
     * 
     * 
     * private String getNextId() { return Integer.toString(id++); }
     *//**
     * @return the parsed test run session, or <code>null</code>
     */
    /*
     * public SubstepsRunSession getTestRunSession() { return
     * substepsRunSession; }
     */
}
