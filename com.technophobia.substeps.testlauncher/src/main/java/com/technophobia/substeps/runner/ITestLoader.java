package com.technophobia.substeps.runner;

public interface ITestLoader {
    /**
     * @param testClasses
     *            classes to be run
     * @param testName
     *            individual method to be run
     * @param failureNames
     *            may want to run these first, since they failed
     * @param listener
     *            to be notified if tests could not be loaded
     * @return the loaded test references
     */
    ITestReference[] loadTests(Class<?>[] testClasses, String testName, String[] failureNames, RemoteTestRunner listener);
}
