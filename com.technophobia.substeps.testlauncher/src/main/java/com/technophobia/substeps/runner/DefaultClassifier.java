package com.technophobia.substeps.runner;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultClassifier implements IClassifiesThrowables {

    private final String version;


    public DefaultClassifier(final String version) {
        this.version = version;
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.internal.junit.runner.ThrowableClassifier#getTrace(java
     * .lang.Throwable)
     */
    @Override
    public String getTrace(final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        final StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jdt.internal.junit.runner.ThrowableClassifier#isComparisonFailure
     * (java.lang.Throwable)
     */
    @Override
    public boolean isComparisonFailure(final Throwable throwable) {
        if (!version.equals("3")) //$NON-NLS-1$
            return false;
        // avoid reference to comparison failure to avoid a dependency on 3.8.1
        return throwable.getClass().getName().equals("junit.framework.ComparisonFailure"); //$NON-NLS-1$
    }
}
