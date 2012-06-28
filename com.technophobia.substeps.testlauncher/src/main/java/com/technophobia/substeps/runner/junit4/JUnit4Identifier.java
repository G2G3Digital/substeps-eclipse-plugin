package com.technophobia.substeps.runner.junit4;

import org.junit.runner.Description;

import com.technophobia.substeps.runner.ITestIdentifier;

public class JUnit4Identifier implements ITestIdentifier {
    private final Description plan;


    public JUnit4Identifier(final Description plan) {
        this.plan = plan;
    }


    @Override
    public String getName() {
        return plan.getDisplayName();
    }


    @Override
    public int hashCode() {
        return plan.hashCode();
    }


    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JUnit4Identifier))
            return false;

        final JUnit4Identifier id = (JUnit4Identifier) obj;
        return plan.equals(id.plan);
    }

}
