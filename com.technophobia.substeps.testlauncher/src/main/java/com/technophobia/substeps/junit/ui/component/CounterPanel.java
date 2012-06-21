package com.technophobia.substeps.junit.ui.component;

public interface CounterPanel {

    void setTotal(int totalCount);


    void setRunValue(int startedCount, int ignoredCount);


    void setErrorValue(int errorCount);


    void setFailureValue(int failureCount);


    void setLayoutData(Object layoutData);


    boolean isDisposed();

}
