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
package com.technophobia.substeps.runner.junit4;

import org.junit.runner.Description;
import org.junit.runner.Request;

import com.technophobia.substeps.runner.ITestIdentifier;
import com.technophobia.substeps.runner.IVisitsTestTrees;

public class JUnit4TestMethodReference extends JUnit4TestReference {
    private final Description description;


    public JUnit4TestMethodReference(final Class<?> clazz, final String methodName, final String[] failureNames) {
        super(createRequest(clazz, methodName), failureNames);
        this.description = Description.createTestDescription(clazz, methodName);
    }


    private static Request createRequest(final Class<?> clazz, final String methodName) {
        final Description method = Description.createTestDescription(clazz, methodName);
        return Request.classWithoutSuiteMethod(clazz).filterWith(method);
    }


    @Override
    public int countTestCases() {
        return 1;
    }


    @Override
    public void sendTree(final IVisitsTestTrees notified) {
        notified.visitTreeEntry(getIdentifier(), false, 1);
    }


    public String getName() {
        return description.toString();
    }


    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JUnit4TestMethodReference))
            return false;

        final JUnit4TestMethodReference ref = (JUnit4TestMethodReference) obj;
        return (ref.description.equals(description));
    }


    @Override
    public int hashCode() {
        return description.hashCode();
    }


    @Override
    public String toString() {
        return description.toString();
    }


    @Override
    public ITestIdentifier getIdentifier() {
        return new JUnit4Identifier(description);
    }
}
