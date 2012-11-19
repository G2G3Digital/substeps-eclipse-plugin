package com.technophobia.substeps.editor.outline.model;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.text.Position;

public class ScenarioOutlineElement extends AbstractStepContainerElement {

    private ExampleElement example;


    public ScenarioOutlineElement(final String text, final Position position) {
        super(text, position);
    }


    public void setExample(final ExampleElement example) {
        this.example = example;
        this.example.setParent(this);
    }


    @Override
    public Collection<AbstractModelElement> getChildren() {
        final Collection<AbstractModelElement> children = super.getChildren();
        if (example != null) {
            final Collection<AbstractModelElement> updated = new ArrayList<AbstractModelElement>(children);
            updated.add(example);
            return updated;
        }
        return children;
    }


    @Override
    public String toString() {
        return "Scenario Outline:" + getText() + "(" + getPosition() + ")";
    }
}
