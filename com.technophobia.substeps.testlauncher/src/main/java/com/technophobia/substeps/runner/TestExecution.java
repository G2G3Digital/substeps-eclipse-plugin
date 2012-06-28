package com.technophobia.substeps.runner;

import java.util.ArrayList;
import java.util.Iterator;

public class TestExecution {
    private boolean shouldStop = false;

    private final IListensToTestExecutions executionListener;

    private final IClassifiesThrowables classifier;

    private final ArrayList<IStopListener> stopListeners = new ArrayList<IStopListener>();


    public TestExecution(final IListensToTestExecutions listener, final IClassifiesThrowables classifier) {
        this.classifier = classifier;
        this.executionListener = listener;
    }


    public void run(final ITestReference[] suites) {
        for (int i = 0; i < suites.length; i++) {
            if (shouldStop)
                return;
            suites[i].run(this);
        }
    }


    public boolean shouldStop() {
        return shouldStop;
    }


    public void stop() {
        shouldStop = true;
        for (final Iterator<IStopListener> iter = stopListeners.iterator(); iter.hasNext();) {
            final IStopListener listener = iter.next();
            listener.stop();
        }
    }


    public IListensToTestExecutions getListener() {
        return executionListener;
    }


    public IClassifiesThrowables getClassifier() {
        return classifier;
    }


    public void addStopListener(final IStopListener listener) {
        stopListeners.add(listener);
    }
}
