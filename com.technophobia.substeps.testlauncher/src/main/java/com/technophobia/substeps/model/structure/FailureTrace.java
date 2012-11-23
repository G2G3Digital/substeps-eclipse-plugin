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

/**
 * A failure trace of a test.
 * 
 * This class is not intended to be instantiated or extended by clients.
 */
public class FailureTrace {
    private final String actual;
    private final String expected;
    private final String trace;


    public FailureTrace(final String trace, final String expected, final String actual) {
        this.actual = actual;
        this.expected = expected;
        this.trace = trace;
    }


    /**
     * Returns the failure stack trace.
     * 
     * @return the failure stack trace
     */
    public String getTrace() {
        return trace;
    }


    /**
     * Returns the expected result or <code>null</code> if the trace is not a
     * comparison failure.
     * 
     * @return the expected result or <code>null</code> if the trace is not a
     *         comparison failure.
     */
    public String getExpected() {
        return expected;
    }


    /**
     * Returns the actual result or <code>null</code> if the trace is not a
     * comparison failure.
     * 
     * @return the actual result or <code>null</code> if the trace is not a
     *         comparison failure.
     */
    public String getActual() {
        return actual;
    }
}
