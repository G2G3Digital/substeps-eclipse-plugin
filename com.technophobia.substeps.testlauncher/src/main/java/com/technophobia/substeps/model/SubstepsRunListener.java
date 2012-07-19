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
