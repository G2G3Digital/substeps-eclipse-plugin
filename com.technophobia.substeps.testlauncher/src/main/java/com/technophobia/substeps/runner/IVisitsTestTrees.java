package com.technophobia.substeps.runner;

public interface IVisitsTestTrees {

    void visitTreeEntry(ITestIdentifier identifier, boolean hasChildren, int testCount);
}
