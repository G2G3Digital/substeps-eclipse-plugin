package com.technophobia.substeps.junit.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.core.IJavaProject;

import com.technophobia.substeps.model.SubstepsSessionListener;
import com.technophobia.substeps.model.structure.Result;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestElementContainer;
import com.technophobia.substeps.model.structure.SubstepsTestParentElement;
import com.technophobia.substeps.model.structure.SubstepsTestRootElement;

public interface SubstepsRunSession extends TestRunStats, SubstepsTestElementContainer {

    SubstepsTestElement[] getAllFailedTestElements();


    ILaunch getLaunch();


    boolean isKeptAlive();


    boolean isRunning();


    void stopTestRun();


    boolean isStarting();


    void addTestSessionListener(SubstepsSessionListener sessionListener);


    void removeTestSessionListener(SubstepsSessionListener sessionListener);


    void swapOut();


    boolean isStopped();


    long getStartTime();


    @Override
    int getStartedCount();


    SubstepsTestElement getTestElement(String testId);


    boolean rerunTest(String testId, String className, String testName, String launchMode, boolean buildBeforeLaunch)
            throws CoreException;


    String getTestRunnerKind();


    @Override
    int getTotalCount();


    void removeSwapFile();


    void reset();


    SubstepsTestRootElement getTestRoot();


    SubstepsTestElement createTestElement(SubstepsTestParentElement rootElement, String nextId, String suiteName,
            boolean b, int i);


    void registerTestEnded(SubstepsTestElement testElement, boolean completed);


    void registerTestFailureStatus(SubstepsTestElement testElement, Status status, String string, String string2,
            String string3);


    Result getTestResult(boolean includeChildren);


    @Override
    SubstepsTestElement[] getChildren();


    FailureTrace getFailureTrace();


    SubstepsRunSession getSubstepsRunSession();


    IJavaProject getLaunchedProject();


    String getTestRunName();


    double getElapsedTimeInSeconds();

}
