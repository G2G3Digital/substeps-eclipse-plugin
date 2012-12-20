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

public class JUnit4TestClassReference extends JUnit4TestReference {

    protected final Class<?> clazz;


    public JUnit4TestClassReference(final Class<?> clazz, final String[] failureNames) {
        super(Request.aClass(clazz), failureNames);
        this.clazz = clazz;
    }


    @Override
    public int countTestCases() {
        return runner.testCount();
    }


    public String getName() {
        return clazz.getName();
    }


    @Override
    public void sendTree(final IVisitsTestTrees notified) {
        sendDescriptionTree(notified, runner.getDescription());
    }


    private void sendDescriptionTree(final IVisitsTestTrees notified, final org.junit.runner.Description description) {
        if (description.isTest()) {
            notified.visitTreeEntry(new JUnit4Identifier(description), false, 1);
        } else {
            notified.visitTreeEntry(new JUnit4Identifier(description), true, description.getChildren().size());
            for (final Description child : description.getChildren()) {
                sendDescriptionTree(notified, child);
            }
        }
    }


    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JUnit4TestReference))
            return false;

        final JUnit4TestReference ref = (JUnit4TestReference) obj;
        return (ref.getIdentifier().equals(getIdentifier()));
    }


    @Override
    public int hashCode() {
        return clazz.hashCode();
    }


    @Override
    public ITestIdentifier getIdentifier() {
        return new JUnit4Identifier(runner.getDescription());
    }
}
