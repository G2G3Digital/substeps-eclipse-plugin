package com.technophobia.substeps.editor.outline.model;

import org.eclipse.jface.text.Position;

public class SubstepsDefinitionElement extends AbstractStepContainerElement {

    public SubstepsDefinitionElement(final String text, final Position position) {
        super(text, position);
    }


    @Override
    public String toString() {
        return "Substeps Definition:" + getText() + "(" + getPosition() + ")";
    }
}
