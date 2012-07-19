package com.technophobia.substeps.model.structure;

public class SubstepsTestLeafElement extends AbstractSubstepsTestElement {

    private boolean ignored;


    public SubstepsTestLeafElement(final SubstepsTestParentElement parent, final String id, final String testName) {
        super(parent, id, testName);
    }


    public String getTestMethodName() {
        return getTestName();
    }


    @Override
    public Result getTestResult(final boolean includeChildren) {
        if (ignored) {
            return Result.IGNORED;
        }
        return super.getTestResult(includeChildren);
    }


    public boolean isIgnored() {
        return ignored;
    }


    public void setIgnored(final boolean ignored) {
        this.ignored = ignored;
    }
}
