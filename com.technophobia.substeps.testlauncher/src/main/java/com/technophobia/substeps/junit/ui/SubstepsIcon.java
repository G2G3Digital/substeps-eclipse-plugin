package com.technophobia.substeps.junit.ui;

public enum SubstepsIcon {

    StackView("eview16/stackframe.gif"), //
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


    private SubstepsIcon(final String path) {
        this.path = path;
    }


    public String getPath() {
        return path;
    }
}
