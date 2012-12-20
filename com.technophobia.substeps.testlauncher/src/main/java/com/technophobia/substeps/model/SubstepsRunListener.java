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
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;

public interface SubstepsRunListener {

    void testRunStarted(int totalCount);


    void testRunTerminated();


    void testRunStopped(long elapsedTime);


    void testRunEnded(long elapsedTime);


    void testStarted(String id, String testName);


    void testFailed(Status status, String id, String testName, String trace, String expected, String actual);


    void testEnded(String id, String testName);


    void testReran(String id, String className, String testMethodName, Status status, String trace, String expected,
            String actual);


    void testTreeEntry(String description);


    void sessionLaunched(SubstepsRunSession substepsRunSession);


    void sessionStarted(SubstepsRunSession session);


    void sessionFinished(SubstepsRunSession session);


    void testCaseStarted(SubstepsTestLeafElement testCaseElement);


    void testCaseFinished(SubstepsTestLeafElement testCaseElement);

}
