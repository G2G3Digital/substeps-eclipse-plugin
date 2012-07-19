package com.technophobia.substeps.junit.ui;

public class JunitTestRunSessionAdapter implements TestRunStats {

    public JunitTestRunSessionAdapter() {
    }


    @Override
    public int getTotalCount() {
        throw new UnsupportedOperationException();
    }


    @Override
    public int getStartedCount() {
        throw new UnsupportedOperationException();
    }


    @Override
    public int getIgnoredCount() {
        throw new UnsupportedOperationException();
    }


    @Override
    public int getErrorCount() {
        throw new UnsupportedOperationException();
    }


    @Override
    public int getFailureCount() {
        throw new UnsupportedOperationException();
    }


    @Override
    public TestRunState getState() {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean hasErrorsOrFailures() {
        return getFailureCount() + getErrorCount() > 0;
    }

}
