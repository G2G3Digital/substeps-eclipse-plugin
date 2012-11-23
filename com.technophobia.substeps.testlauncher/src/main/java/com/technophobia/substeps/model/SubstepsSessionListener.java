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
package com.technophobia.substeps.model;

import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;

public interface SubstepsSessionListener {

    /**
     * A test run has started.
     */
    public void sessionStarted();


    /**
     * A test run has ended.
     * 
     * @param elapsedTime
     *            the total elapsed time of the test run
     */
    public void sessionEnded(long elapsedTime);


    /**
     * A test run has been stopped prematurely.
     * 
     * @param elapsedTime
     *            the time elapsed before the test run was stopped
     */
    public void sessionStopped(long elapsedTime);


    /**
     * The VM instance performing the tests has terminated.
     */
    public void sessionTerminated();


    /**
     * A test has been added to the plan.
     * 
     * @param testElement
     *            the test
     */
    public void testAdded(SubstepsTestElement testElement);


    /**
     * All test have been added and running begins
     */
    public void runningBegins();


    /**
     * An individual test has started.
     * 
     * @param testCaseElement
     *            the test
     */
    public void testStarted(SubstepsTestLeafElement testCaseElement);


    /**
     * An individual test has ended.
     * 
     * @param testCaseElement
     *            the test
     */
    public void testEnded(SubstepsTestLeafElement testCaseElement);


    /**
     * An individual test has failed with a stack trace.
     * 
     * @param testElement
     *            the test
     * @param status
     *            the outcome of the test; one of
     *            {@link SubstepsTestElement.Status#ERROR} or
     *            {@link SubstepsTestElement.Status#FAILURE}
     * @param trace
     *            the stack trace
     * @param expected
     *            expected value
     * @param actual
     *            actual value
     */
    public void testFailed(SubstepsTestElement testElement, Status status, String trace, String expected, String actual);


    /**
     * An individual test has been rerun.
     * 
     * @param testCaseElement
     *            the test
     * @param status
     *            the outcome of the test that was rerun; one of
     *            {@link Status#OK}, {@link Status#ERROR}, or
     *            {@link Status#FAILURE}
     * @param trace
     *            the stack trace in the case of abnormal termination, or the
     *            empty string if none
     * @param expectedResult
     *            expected value
     * @param actualResult
     *            actual value
     */
    public void testReran(SubstepsTestLeafElement testCaseElement, Status status, String trace, String expectedResult,
            String actualResult);


    /**
     * @return <code>true</code> if the test run session can be swapped to disk
     *         although this listener is still installed
     */
    public boolean acceptsSwapToDisk();


    public void sessionLaunched(SubstepsRunSession substepsRunSession);

}
