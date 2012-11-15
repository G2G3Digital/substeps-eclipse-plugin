package com.technophobia.substeps.editor.outline.model;

import org.eclipse.jface.text.Position;

public class BackgroundElement extends AbstractStepContainerElement {

    public BackgroundElement(final String text, final Position position) {
        super(text, position);
    }


    @Override
    public String toString() {
        return "Background :" + getText() + "(" + getPosition() + ")";
    }
}
