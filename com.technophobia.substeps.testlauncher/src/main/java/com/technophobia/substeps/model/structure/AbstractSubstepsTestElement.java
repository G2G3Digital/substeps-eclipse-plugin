/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.model.structure;

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


    @Override
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


    @Override
    public String toString() {
        return id + ": " + testName;
    }


    private void updateTime(final Status s) {
        if (s.equals(Status.RUNNING)) {
            time = -System.currentTimeMillis() / 1000d;
        } else if (s.isComplete()) {
            if (time < 0) {
                final double endTime = System.currentTimeMillis() / 1000.0d;
                time = endTime + time;
            }
        }
    }
}
