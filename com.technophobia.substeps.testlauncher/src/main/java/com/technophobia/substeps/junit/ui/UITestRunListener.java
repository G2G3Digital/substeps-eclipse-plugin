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
package com.technophobia.substeps.junit.ui;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.model.SubstepsRunListener;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;

public class UITestRunListener implements SubstepsRunListener {

    @Override
    public void sessionLaunched(final SubstepsRunSession substepsRunSession) {
        FeatureRunnerPlugin.instance().asyncShowSubstepsRunnerViewPart();
    }


    @Override
    public void testRunStarted(final int totalCount) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testRunTerminated() {
        // TODO Auto-generated method stub

    }


    @Override
    public void testRunStopped(final long elapsedTime) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testRunEnded(final long elapsedTime) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testStarted(final String id, final String testName) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testFailed(final Status status, final String id, final String testName, final String trace,
            final String expected, final String actual) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testEnded(final String id, final String testName) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testReran(final String id, final String className, final String testMethodName, final Status status,
            final String trace, final String expected, final String actual) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testTreeEntry(final String description) {
        // TODO Auto-generated method stub

    }


    @Override
    public void sessionStarted(final SubstepsRunSession session) {
        // TODO Auto-generated method stub

    }


    @Override
    public void sessionFinished(final SubstepsRunSession session) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testCaseStarted(final SubstepsTestLeafElement testCaseElement) {
        // TODO Auto-generated method stub

    }


    @Override
    public void testCaseFinished(final SubstepsTestLeafElement testCaseElement) {
        // TODO Auto-generated method stub

    }
}
