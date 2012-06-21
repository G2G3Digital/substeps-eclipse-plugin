package com.technophobia.substeps.junit.ui;

import org.eclipse.jdt.internal.junit.model.TestRunSession;

import com.technophobia.eclipse.transformer.Supplier;

public class JunitTestRunSessionAdapter implements TestRunStats {

    private final Supplier<TestRunSession> delegate;


    public JunitTestRunSessionAdapter(final Supplier<TestRunSession> delegate) {
        this.delegate = delegate;
    }


    @Override
    public int getTotalCount() {
        return delegate.get().getTotalCount();
    }


    @Override
    public int getStartedCount() {
        return delegate.get().getStartedCount();
    }


    @Override
    public int getIgnoredCount() {
        return delegate.get().getIgnoredCount();
    }


    @Override
    public int getErrorCount() {
        return delegate.get().getErrorCount();
    }


    @Override
    public int getFailureCount() {
        return delegate.get().getFailureCount();
    }


    @Override
    public TestRunState getState() {
        return delegate.get().isStopped() ? TestRunState.STOPPED : TestRunState.IN_PROGRESS;
    }


    @Override
    public boolean hasErrorsOrFailures() {
        return getFailureCount() + getErrorCount() > 0;
    }

}
