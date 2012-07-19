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
