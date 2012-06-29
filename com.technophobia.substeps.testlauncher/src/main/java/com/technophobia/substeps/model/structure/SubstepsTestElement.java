package com.technophobia.substeps.model.structure;

import com.technophobia.substeps.junit.ui.SubstepsRunSession;

public interface SubstepsTestElement {

    String getTestName();


    boolean isComparisonFailure();


    String getTrace();


    String getId();


    String getClassName();


    SubstepsTestParentElement getParent();


    void setStatus(Status status, String trace, String expected, String actual);


    Status getStatus();


    void setStatus(Status status);


    Result getTestResult(final boolean includeChildren);


    FailureTrace getFailureTrace();


    SubstepsRunSession getSubstepsRunSession();


    double getElapsedTimeInSeconds();


    SubstepsTestElementContainer getParentContainer();


    SubstepsTestRootElement getRoot();
}