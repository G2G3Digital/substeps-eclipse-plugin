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
