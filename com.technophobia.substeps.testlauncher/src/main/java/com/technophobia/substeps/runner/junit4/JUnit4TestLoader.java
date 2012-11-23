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

import com.technophobia.substeps.runner.ITestLoader;
import com.technophobia.substeps.runner.ITestReference;
import com.technophobia.substeps.runner.RemoteTestRunner;

public class JUnit4TestLoader implements ITestLoader {

    @Override
    public ITestReference[] loadTests(@SuppressWarnings("rawtypes") final Class[] testClasses, // https://bugs.eclipse.org/bugs/show_bug.cgi?id=164472
            final String testName, final String[] failureNames, final RemoteTestRunner listener) {

        final ITestReference[] refs = new ITestReference[testClasses.length];
        for (int i = 0; i < testClasses.length; i++) {
            final Class<?> clazz = testClasses[i];
            final ITestReference ref = createTest(clazz, testName, failureNames);
            refs[i] = ref;
        }
        return refs;
    }


    private ITestReference createTest(final Class<?> clazz, final String testName, final String[] failureNames) {
        if (clazz == null)
            return null;
        if (testName == null)
            return new JUnit4TestClassReference(clazz, failureNames);

        return new JUnit4TestMethodReference(clazz, testName, failureNames);
    }
}
