package com.technophobia.substeps.junit.ui.component;

import org.eclipse.swt.widgets.Composite;

public class JUnitDelegateCounterPanel implements CounterPanel {

    private final org.eclipse.jdt.internal.junit.ui.CounterPanel delegate;


    public JUnitDelegateCounterPanel(final Composite parent) {
        this.delegate = new org.eclipse.jdt.internal.junit.ui.CounterPanel(parent);
    }


    @Override
    public void setTotal(final int totalCount) {
        delegate.setTotal(totalCount);
    }


    @Override
    public void setRunValue(final int startedCount, final int ignoredCount) {
        delegate.setRunValue(startedCount, ignoredCount);
    }


    @Override
    public void setErrorValue(final int errorCount) {
        delegate.setErrorValue(errorCount);
    }


    @Override
    public void setFailureValue(final int failureCount) {
        delegate.setErrorValue(failureCount);
    }


    @Override
    public void setLayoutData(final Object layoutData) {
        delegate.setLayoutData(layoutData);
    }


    @Override
    public boolean isDisposed() {
        return delegate.isDisposed();
    }
}
