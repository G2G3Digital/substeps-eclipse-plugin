package com.technophobia.substeps.editor.outline.model;

import org.eclipse.jface.text.Position;

public class ScenarioElement extends AbstractStepContainerElement {

    public ScenarioElement(final String text, final Position position) {
        super(text, position);
    }


    @Override
    public String toString() {
        return "Scenario:" + getText() + "(" + getPosition() + ")";
    }
}
