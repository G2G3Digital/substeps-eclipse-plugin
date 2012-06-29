package com.technophobia.substeps.junit.ui;

public enum SubstepsFailureTraceIcon implements SubstepsIcon {

    StackView("eview16/stackframe.gif"), //
    Stack("obj16/stkfrm_obj.gif"), //
    Exception("obj16/exc_catch.gif"), //
    Failures("obj16/failures.gif");

    private final String path;


    private SubstepsFailureTraceIcon(final String path) {
        this.path = path;
    }


    @Override
    public String getPath() {
        return path;
    }
}
