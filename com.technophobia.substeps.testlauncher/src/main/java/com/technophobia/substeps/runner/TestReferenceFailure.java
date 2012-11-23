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
