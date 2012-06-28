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
    SuiteRunning("obj16/tsuiterun.gif"), //
    Exception("obj16/exc_catch.gif"), //
    Failures("obj16/failures.gif"), //
    StopEnabled("elcl16/stop.gif"), //
    StopDisbled("dlcl16/stop.gif"), //
    RelaunchEnabled("elc16/relaunch.gif"), //
    RelaunchDisabled("dlc16/relaunch.gif"), //
    RelaunchFailedEnabled("elc16/relaunchf.gif"), //
    RelaunchFailedDisabled("dlc16/relaunchf.gif"), //
    Stack("obj16/stkfrm_obj.gif"), //
    StackFilterEnabled("elcl16/cfilter.gif"), //
    StackFilterDisabled("dlcl16/cfilter.gif"), //
    ScrollLockEnabled("elcl16/lock.gif"), //
    ScrollLockDisabled("dlcl16/lock.gif"), //
    OrientationHorizontal("elcl16/th_horizontal.gif"), //
    OrientationVertical("elcl16/th_vertical.gif"), //
    OrientationAutomatic("elcl16/th_automatic.gif"), //
    Compare("dlcl16/compare.gif"), //
    HierarchicalLayout("elcl16/hierarchicalLayout.gif"), //
    HistoryListEnabled("elcl16/history_list.gif"), //
    HistoryListDisabled("dlcl16/history_list.gif"), //
    SelectPreviousTestEnabled("elcl16/select_prev.gif"), //
    SelectPreviousTestDisabled("dlcl16/select_prev.gif"), //
    SelectNextTestEnabled("elcl16/select_next.gif"), //
    SelectNextTestDisabled("dlcl16/select_next.gif");

    private final String path;


    private SubstepsIcon(final String path) {
        this.path = path;
    }


    public String getPath() {
        return path;
    }
}
