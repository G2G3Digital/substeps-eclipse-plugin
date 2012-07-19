package com.technophobia.substeps.model.structure;

public enum Result {
    UNDEFINED("Undefined"), //
    OK("Ok"), //
    ERROR("Error"), //
    FAILURE("Failure"), //
    IGNORED("Ignored");

    private final String value;


    private Result(final String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
