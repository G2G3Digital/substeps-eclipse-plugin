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
