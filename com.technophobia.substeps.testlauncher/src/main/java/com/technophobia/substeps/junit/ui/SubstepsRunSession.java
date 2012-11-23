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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.core.IJavaProject;

import com.technophobia.substeps.model.SubstepsSessionListener;
import com.technophobia.substeps.model.structure.Result;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestElementContainer;
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
