package com.technophobia.substeps.runner;

public interface ITestReference {

    int countTestCases();


    void sendTree(IVisitsTestTrees notified);


    void run(TestExecution execution);


    ITestIdentifier getIdentifier();
}
