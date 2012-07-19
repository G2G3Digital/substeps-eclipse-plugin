package com.technophobia.substeps.runner;

public interface IListensToTestExecutions {
    void notifyTestFailed(TestReferenceFailure failure);


    void notifyTestStarted(ITestIdentifier test);


    void notifyTestEnded(ITestIdentifier test);
}
