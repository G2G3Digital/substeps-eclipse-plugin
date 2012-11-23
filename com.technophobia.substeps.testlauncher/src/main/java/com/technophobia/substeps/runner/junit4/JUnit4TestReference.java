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


    public JUnit4TestReference(final Request req, final String[] failureNames) {
        final Request request = failureNames != null ? req.sortWith(new FailuresFirstSorter(failureNames)) : req;
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
