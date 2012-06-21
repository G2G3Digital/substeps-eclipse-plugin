package com.technophobia.substeps.junit.ui;

public class TestContext {

    private final String testId;
    private final String className;
    private final String testName;
    private final String launchMode;


    public TestContext(final String testId, final String className, final String testName, final String launchMode) {
        super();
        this.testId = testId;
        this.className = className;
        this.testName = testName;
        this.launchMode = launchMode;
    }


    public String getTestId() {
        return testId;
    }


    public String getClassName() {
        return className;
    }


    public String getTestName() {
        return testName;
    }


    public String getLaunchMode() {
        return launchMode;
    }
}
