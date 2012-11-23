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

public enum SubstepsTestIcon implements SubstepsIcon {

    TestRunOK("eview16/junitsucc.gif"), //
    TestRunFail("eview16/juniterr.gif"), //
    TestRunOKDirty("eview16/junitsuccq.gif"), //
    TestRunFailDirty("eview16/juniterrq.gif"), //
    Test("obj16/test.gif"), //
    TestOk("obj16/testok.gif"), //
    TestError("obj16/testerr.gif"), //
    TestFail("obj16/testfail.gif"), //
    TestRunning("obj16/testrun.gif"), //
    TestIgnored("obj16/testignored.gif"), //
    Suite("obj16/tsuite.gif"), //
    SuiteOk("obj16/tsuiteok.gif"), //
    SuiteError("obj16/tsuiteerror.gif"), //
    SuiteFail("obj16/tsuitefail.gif"), //
    SuiteRunning("obj16/tsuiterun.gif");

    private final String path;


    private SubstepsTestIcon(final String path) {
        this.path = path;
    }


    @Override
    public String getPath() {
        return path;
    }
}
