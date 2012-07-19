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
