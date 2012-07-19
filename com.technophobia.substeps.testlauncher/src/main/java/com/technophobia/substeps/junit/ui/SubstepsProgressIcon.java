package com.technophobia.substeps.junit.ui;

public enum SubstepsProgressIcon implements SubstepsIcon {

    OK0("prgss/ss0.gif"), //
    OK1("prgss/ss1.gif"), //
    OK2("prgss/ss2.gif"), //
    OK3("prgss/ss3.gif"), //
    OK4("prgss/ss4.gif"), //
    OK5("prgss/ss5.gif"), //
    OK6("prgss/ss6.gif"), //
    OK7("prgss/ss7.gif"), //
    OK8("prgss/ss8.gif"), //
    OK9("prgss/ss9.gif"), //
    FAILURE0("prgss/ff0.gif"), //
    FAILURE1("prgss/ff1.gif"), //
    FAILURE2("prgss/ff2.gif"), //
    FAILURE3("prgss/ff3.gif"), //
    FAILURE4("prgss/ff4.gif"), //
    FAILURE5("prgss/ff5.gif"), //
    FAILURE6("prgss/ff6.gif"), //
    FAILURE7("prgss/ff7.gif"), //
    FAILURE8("prgss/ff8.gif"), //
    FAILURE9("prgss/ff9.gif");

    private final String path;


    private SubstepsProgressIcon(final String path) {
        this.path = path;
    }


    @Override
    public String getPath() {
        return path;
    }
}
