package com.technophobia.substeps.editor.outline.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.text.Position;

public abstract class AbstractStepContainerElement extends AbstractModelElement {

    private final Collection<StepElement> steps;


    public AbstractStepContainerElement(final String text, final Position position) {
        super(text, position);
        this.steps = new ArrayList<StepElement>();
    }


    public void addStep(final StepElement stepElement) {
        stepElement.setParent(this);
        steps.add(stepElement);
    }


    @Override
    public Collection<AbstractModelElement> getChildren() {
        return Collections.<AbstractModelElement> unmodifiableCollection(steps);
    }
}
