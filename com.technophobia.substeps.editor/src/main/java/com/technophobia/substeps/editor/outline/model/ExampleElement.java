package com.technophobia.substeps.editor.outline.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.text.Position;

public class ExampleElement extends AbstractModelElement {

    private final Collection<ExampleRowElement> exampleRows;


    public ExampleElement(final String text, final Position position) {
        super(text, position);
        this.exampleRows = new ArrayList<ExampleRowElement>();
    }


    public void addExampleRow(final ExampleRowElement exampleRowElement) {
        exampleRowElement.setParent(this);
        this.exampleRows.add(exampleRowElement);
    }


    @Override
    public Collection<AbstractModelElement> getChildren() {
        return Collections.<AbstractModelElement> unmodifiableCollection(exampleRows);
    }


    @Override
    public String toString() {
        return "Example:" + getText() + "(" + getPosition() + ")";
    }
}
