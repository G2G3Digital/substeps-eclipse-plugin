package com.technophobia.substeps.runner;

public class TestReferenceFailure {

    private final ITestIdentifier test;

    private final String trace;

    private final String status;

    private FailedComparison comparison;


    public TestReferenceFailure(final ITestIdentifier ref, final String status, final String trace) {
        this(ref, status, trace, null);
    }


    public TestReferenceFailure(final ITestIdentifier reference, final String status, final String trace,
            final FailedComparison comparison) {
        this.test = reference;
        this.status = status;
        this.trace = trace;
        this.comparison = comparison;
    }


    public TestReferenceFailure(final ITestReference reference, final String status, final String trace) {
        this(reference.getIdentifier(), status, trace);
    }


    public String getStatus() {
        return status;
    }


    public String getTrace() {
        return trace;
    }


    public ITestIdentifier getTest() {
        return test;
    }


    @Override
    public String toString() {
        return status + " " + test.getName(); //$NON-NLS-1$
    }


    public void setComparison(final FailedComparison comparison) {
        this.comparison = comparison;
    }


    public FailedComparison getComparison() {
        return comparison;
    }
}
