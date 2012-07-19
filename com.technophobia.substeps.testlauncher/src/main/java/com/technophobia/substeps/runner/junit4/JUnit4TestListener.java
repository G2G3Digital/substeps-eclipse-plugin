package com.technophobia.substeps.runner.junit4;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.technophobia.substeps.model.MessageIds;
import com.technophobia.substeps.runner.FailedComparison;
import com.technophobia.substeps.runner.IListensToTestExecutions;
import com.technophobia.substeps.runner.ITestIdentifier;
import com.technophobia.substeps.runner.TestReferenceFailure;

public class JUnit4TestListener extends RunListener {

    private static class IgnoredTestIdentifier extends JUnit4Identifier {
        public IgnoredTestIdentifier(final Description description) {
            super(description);
        }


        @Override
        public String getName() {
            final String name = super.getName();
            if (name != null)
                return MessageIds.IGNORED_TEST_PREFIX + name;
            return null;
        }
    }

    private final IListensToTestExecutions notified;


    public JUnit4TestListener(final IListensToTestExecutions notified) {
        this.notified = notified;
    }


    @Override
    public void testStarted(final Description plan) throws Exception {
        notified.notifyTestStarted(getIdentifier(plan));
    }


    @Override
    public void testFailure(final Failure failure) throws Exception {
        TestReferenceFailure testReferenceFailure;
        try {
            final Throwable exception = failure.getException();
            final String status = exception instanceof AssertionError ? MessageIds.TEST_FAILED : MessageIds.TEST_ERROR;
            FailedComparison comparison = null;
            if (exception instanceof junit.framework.ComparisonFailure) {
                final junit.framework.ComparisonFailure comparisonFailure = (junit.framework.ComparisonFailure) exception;
                comparison = new FailedComparison(comparisonFailure.getExpected(), comparisonFailure.getActual());
            } else if (exception instanceof org.junit.ComparisonFailure) {
                final org.junit.ComparisonFailure comparisonFailure = (org.junit.ComparisonFailure) exception;
                comparison = new FailedComparison(comparisonFailure.getExpected(), comparisonFailure.getActual());
            }
            testReferenceFailure = new TestReferenceFailure(getIdentifier(failure.getDescription()), status,
                    failure.getTrace(), comparison);
        } catch (final RuntimeException e) {
            final StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            testReferenceFailure = new TestReferenceFailure(getIdentifier(failure.getDescription()),
                    MessageIds.TEST_FAILED, stringWriter.getBuffer().toString(), null);
        }
        notified.notifyTestFailed(testReferenceFailure);
    }


    @Override
    public void testIgnored(final Description plan) throws Exception {
        // Send message to listeners which would be stale otherwise
        final ITestIdentifier identifier = new IgnoredTestIdentifier(plan);
        notified.notifyTestStarted(identifier);
        notified.notifyTestEnded(identifier);
    }


    @Override
    public void testFinished(final Description plan) throws Exception {
        notified.notifyTestEnded(getIdentifier(plan));
    }


    private ITestIdentifier getIdentifier(final Description plan) {
        return new JUnit4Identifier(plan);
    }
}
