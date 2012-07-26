package com.technophobia.substeps.model;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.junit.ui.SubstepsRunSessionListener;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;

public class SubstepsRunSessionListenerImpl implements SubstepsRunSessionListener {
    private SubstepsRunSession activeTestRunSession;
    private SubstepsSessionListener substepsSessionListener;


    @Override
    public void sessionAdded(final SubstepsRunSession substepsRunSession) {
        // Only serve one legacy ITestRunListener at a time, since they cannot
        // distinguish between different concurrent test sessions:
        if (activeTestRunSession != null)
            return;

        this.activeTestRunSession = substepsRunSession;

        this.substepsSessionListener = new SubstepsSessionListener() {
            @Override
            public void testAdded(final SubstepsTestElement testElement) {
                // No-op
            }


            @Override
            public void sessionStarted() {
                final Object[] testRunListeners = FeatureRunnerPlugin.instance().getSubstepsRunListeners()
                        .getListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    ((SubstepsRunListener) testRunListeners[i]).testRunStarted(activeTestRunSession.getTotalCount());
                }
            }


            @Override
            public void sessionTerminated() {
                final Object[] testRunListeners = FeatureRunnerPlugin.instance().getSubstepsRunListeners()
                        .getListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    ((SubstepsRunListener) testRunListeners[i]).testRunTerminated();
                }
                sessionRemoved(activeTestRunSession);
            }


            @Override
            public void sessionStopped(final long elapsedTime) {
                final SubstepsRunListener[] testRunListeners = FeatureRunnerPlugin.instance().getModel()
                        .getTestRunListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    testRunListeners[i].testRunStopped(elapsedTime);
                }
                sessionRemoved(activeTestRunSession);
            }


            @Override
            public void sessionEnded(final long elapsedTime) {
                final Object[] testRunListeners = FeatureRunnerPlugin.instance().getSubstepsRunListeners()
                        .getListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    ((SubstepsRunListener) testRunListeners[i]).testRunEnded(elapsedTime);
                }
                sessionRemoved(activeTestRunSession);
            }


            @Override
            public void runningBegins() {
                // ignore
            }


            @Override
            public void testStarted(final SubstepsTestLeafElement testElement) {
                final Object[] testRunListeners = FeatureRunnerPlugin.instance().getSubstepsRunListeners()
                        .getListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    ((SubstepsRunListener) testRunListeners[i]).testStarted(testElement.getId(),
                            testElement.getTestName());
                }
            }


            @Override
            public void testFailed(final SubstepsTestElement testElement, final Status status, final String trace,
                    final String expected, final String actual) {
                final Object[] testRunListeners = FeatureRunnerPlugin.instance().getSubstepsRunListeners()
                        .getListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    ((SubstepsRunListener) testRunListeners[i]).testFailed(status, testElement.getId(),
                            testElement.getTestName(), trace, expected, actual);
                }
            }


            @Override
            public void testEnded(final SubstepsTestLeafElement testCaseElement) {
                final Object[] testRunListeners = FeatureRunnerPlugin.instance().getSubstepsRunListeners()
                        .getListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    ((SubstepsRunListener) testRunListeners[i]).testEnded(testCaseElement.getId(),
                            testCaseElement.getTestName());
                }
            }


            @Override
            public void testReran(final SubstepsTestLeafElement testCaseElement, final Status status,
                    final String trace, final String expectedResult, final String actualResult) {
                final SubstepsRunListener[] testRunListeners = FeatureRunnerPlugin.instance().getModel()
                        .getTestRunListeners();
                for (int i = 0; i < testRunListeners.length; i++) {
                    testRunListeners[i].testReran(testCaseElement.getId(), testCaseElement.getClassName(),
                            testCaseElement.getTestMethodName(), status, trace, expectedResult, actualResult);
                }
            }


            @Override
            public boolean acceptsSwapToDisk() {
                return true;
            }


            @Override
            public void sessionLaunched(final SubstepsRunSession session) {
                // TODO Auto-generated method stub

            }
        };
        activeTestRunSession.addTestSessionListener(substepsSessionListener);
    }


    @Override
    public void sessionRemoved(final SubstepsRunSession testRunSession) {
        if (activeTestRunSession == testRunSession) {
            activeTestRunSession.removeTestSessionListener(substepsSessionListener);
            substepsSessionListener = null;
            activeTestRunSession = null;
        }
    }
}
