package com.technophobia.substeps.model.structure;

public interface SubstepsTestParentElement extends SubstepsTestElement, SubstepsTestElementContainer {

    void addChild(final SubstepsTestElement child);


    void childChangedStatus(final SubstepsTestElement child, final Status childStatus);

}