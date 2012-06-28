package com.technophobia.substeps.model.structure;

import org.eclipse.jdt.internal.junit.model.TestRoot;
import org.eclipse.jdt.junit.model.ITestElementContainer;

import com.technophobia.substeps.junit.ui.SubstepsRunSession;

public abstract class AbstractSubstepsTestElement implements SubstepsTestElement {

    private final SubstepsTestParentElement parent;
    private final String id;
    private final String testName;

    private Status status;
    private String trace;
    private String expected;
    private String actual;

    protected double time = Double.NaN;


    public AbstractSubstepsTestElement(final SubstepsTestParentElement parent, final String id, final String testName) {
        this.parent = parent;
        this.id = id;
        this.testName = testName;
        this.status = Status.NOT_RUN;
        if (parent != null) {
            parent.addChild(this);
        }
    }


    @Override
    public Result getTestResult(final boolean includeChildren) {
        return getStatus().asResult();
    }


    @Override
    public SubstepsRunSession getSubstepsRunSession() {
        return getRoot().getSubstepsRunSession();
    }


    @Override
    public FailureTrace getFailureTrace() {
        final Result result = getTestResult(false);
        if (result.equals(Result.ERROR) || result.equals(Result.FAILURE)) {
            return new FailureTrace(trace, expected, actual);
        }
        return null;
    }


    @Override
    public SubstepsTestParentElement getParent() {
        return parent;
    }


    @Override
    public String getId() {
        return id;
    }


    @Override
    public String getTestName() {
        return testName;
    }


    @Override
    public Status getStatus() {
        return status;
    }


    @Override
    public void setStatus(final Status status) {
        updateTime(status);

        this.status = status;
        final SubstepsTestParentElement p = getParent();
        if (p != null) {
            p.childChangedStatus(this, status);
        }
    }


    @Override
    public void setStatus(final Status status, final String newTrace, final String expected, final String actual) {
        if (this.trace != null && newTrace != null) {
            // don't overwrite original trace - same test can write multiple log
            // entries
            this.trace = trace + newTrace;
        } else {
            this.trace = newTrace;
            this.expected = expected;
            this.actual = actual;
        }
        setStatus(status);
    }


    @Override
    public String getTrace() {
        // TODO Auto-generated method stub
        return trace;
    }


    public String getExpected() {
        return expected;
    }


    public String getActual() {
        return actual;
    }


    @Override
    public boolean isComparisonFailure() {
        return expected != null && actual != null;
    }


    public SubstepsTestRootElement getRoot() {
        return getParent().getRoot();
    }


    @Override
    public double getElapsedTimeInSeconds() {
        return time;
    }


    @Override
    public String getClassName() {
        return null;
    }


    @Override
    public SubstepsTestElementContainer getParentContainer() {
        if (parent instanceof SubstepsTestRootElement) {
            return getSubstepsRunSession();
        }
        return parent;
    }


    private void updateTime(final Status status) {
        if (status.equals(Status.RUNNING)) {
            time = -System.currentTimeMillis() / 1000d;
        } else if (status.isComplete()) {
            if (time < 0) {
                final double endTime = System.currentTimeMillis() / 1000.0d;
                time = endTime + time;
            }
        }
    }
}
