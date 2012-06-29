package com.technophobia.substeps.model.structure;

public class IncompleteParentItem {
    private final SubstepsTestParentElement parentElement;
    private int outstandingChildren;


    public IncompleteParentItem(final SubstepsTestParentElement parentElement, final int outstandingChildren) {
        this.parentElement = parentElement;
        this.outstandingChildren = outstandingChildren;
    }


    public SubstepsTestParentElement getParentElement() {
        return parentElement;
    }


    public void decrementOutstandingChildren() {
        outstandingChildren--;
    }


    public boolean hasOutstandingChildren() {
        return outstandingChildren > 0;
    }
}
