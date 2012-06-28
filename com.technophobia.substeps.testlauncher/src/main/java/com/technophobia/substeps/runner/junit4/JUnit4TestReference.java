package com.technophobia.substeps.runner.junit4;

import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

import com.technophobia.substeps.runner.IStopListener;
import com.technophobia.substeps.runner.ITestReference;
import com.technophobia.substeps.runner.TestExecution;

public abstract class JUnit4TestReference implements ITestReference {
    protected Runner runner;


    public JUnit4TestReference(Request request, final String[] failureNames) {
        if (failureNames != null) {
            request = request.sortWith(new FailuresFirstSorter(failureNames));
        }
        runner = request.getRunner();
    }


    @Override
    public void run(final TestExecution execution) {
        final RunNotifier notifier = new RunNotifier();
        notifier.addListener(new JUnit4TestListener(execution.getListener()));
        execution.addStopListener(new IStopListener() {
            @Override
            public void stop() {
                notifier.pleaseStop();
            }
        });

        final Result result = new Result();
        final RunListener listener = result.createListener();
        notifier.addListener(listener);
        try {
            notifier.fireTestRunStarted(runner.getDescription());
            runner.run(notifier);
            notifier.fireTestRunFinished(result);
        } catch (final StoppedByUserException e) {
            // not interesting, see https://bugs.eclipse.org/329498
        } finally {
            notifier.removeListener(listener);
        }
    }
}
