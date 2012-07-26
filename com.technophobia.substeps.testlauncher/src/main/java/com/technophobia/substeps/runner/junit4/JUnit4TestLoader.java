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
