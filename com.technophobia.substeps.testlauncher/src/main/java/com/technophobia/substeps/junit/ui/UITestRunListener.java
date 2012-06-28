package com.technophobia.substeps.junit.ui;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.model.SubstepsRunListener;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;

public class UITestRunListener implements SubstepsRunListener {

    @Override
    public void sessionLaunched(final SubstepsRunSession substepsRunSession) {
        FeatureRunnerPlugin.instance().asyncShowSubstepsRunnerViewPart();
    }


    @Override
    public void testRunStarted(final int totalCount) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testRunTerminated() {
        // TODO Auto-generated method stub

    }


    @Override
    public void testRunStopped(final long elapsedTime) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testRunEnded(final long elapsedTime) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testStarted(final String id, final String testName) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testFailed(final Status status, final String id, final String testName, final String trace,
            final String expected, final String actual) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testEnded(final String id, final String testName) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testReran(final String id, final String className, final String testMethodName, final Status status,
            final String trace, final String expected, final String actual) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testTreeEntry(final String description) {
        // TODO Auto-generated method stub

    }


    @Override
    public void sessionStarted(final SubstepsRunSession session) {
        // TODO Auto-generated method stub

    }


    @Override
    public void sessionFinished(final SubstepsRunSession session) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testCaseStarted(final SubstepsTestLeafElement testCaseElement) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testCaseFinished(final SubstepsTestLeafElement testCaseElement) {
        // TODO Auto-generated method stub

    }
}
