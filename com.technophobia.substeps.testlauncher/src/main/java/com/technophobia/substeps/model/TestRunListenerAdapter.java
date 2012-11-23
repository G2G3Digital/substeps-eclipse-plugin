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

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;

public class TestRunListenerAdapter implements SubstepsSessionListener {

    private final SubstepsRunSession session;


    public TestRunListenerAdapter(final SubstepsRunSession session) {
        this.session = session;
    }


    private Object[] getListeners() {
        return FeatureRunnerPlugin.instance().getSubstepsRunListeners().getListeners();
    }


    private void fireSessionStarted() {
        final Object[] listeners = getListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((SubstepsRunListener) listeners[i]).sessionStarted(session);
        }
    }


    private void fireSessionFinished() {
        final Object[] listeners = getListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((SubstepsRunListener) listeners[i]).sessionFinished(session);
        }
    }


    private void fireTestCaseStarted(final SubstepsTestLeafElement testCaseElement) {
        final Object[] listeners = getListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((SubstepsRunListener) listeners[i]).testCaseStarted(testCaseElement);
        }
    }


    private void fireTestCaseFinished(final SubstepsTestLeafElement testCaseElement) {
        final Object[] listeners = getListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((SubstepsRunListener) listeners[i]).testCaseFinished(testCaseElement);
        }
    }


    @Override
    public void sessionStarted() {
        // wait until all test are added
    }


    @Override
    public void sessionEnded(final long elapsedTime) {
        fireSessionFinished();
        session.swapOut();
    }


    @Override
    public void sessionStopped(final long elapsedTime) {
        fireSessionFinished();
        session.swapOut();
    }


    @Override
    public void sessionTerminated() {
        session.swapOut();
    }


    @Override
    public void testAdded(final SubstepsTestElement testElement) {
        // do nothing
    }


    @Override
    public void runningBegins() {
        fireSessionStarted();
    }


    @Override
    public void testStarted(final SubstepsTestLeafElement testCaseElement) {
        fireTestCaseStarted(testCaseElement);
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.internal.junit.model.ITestSessionListener#testEnded(org
     * .eclipse.jdt.internal.junit.model.TestCaseElement)
     */
    @Override
    public void testEnded(final SubstepsTestLeafElement testCaseElement) {
        fireTestCaseFinished(testCaseElement);
    }


    @Override
    public void testFailed(final SubstepsTestElement testElement, final Status status, final String trace,
            final String expected, final String actual) {
        // ignore
    }


    @Override
    public void testReran(final SubstepsTestLeafElement testCaseElement, final Status status, final String trace,
            final String expectedResult, final String actualResult) {
        // ignore
    }


    @Override
    public boolean acceptsSwapToDisk() {
        return true;
    }


    @Override
    public void sessionLaunched(final SubstepsRunSession substepsRunSession) {
        // TODO Auto-generated method stub

    }
}
