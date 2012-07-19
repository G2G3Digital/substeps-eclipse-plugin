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
