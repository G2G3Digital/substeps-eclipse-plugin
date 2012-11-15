package com.technophobia.substeps.editor.outline.model;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.text.Position;

public abstract class AbstractModelElement implements Comparable<AbstractModelElement> {

    private final String text;
    private final Position position;

    private AbstractModelElement parent;


    public AbstractModelElement(final String text, final Position position) {
        this.text = text;
        this.position = position;
    }


    public String getText() {
        return text;
    }


    public Position getPosition() {
        return position;
    }


    public AbstractModelElement getParent() {
        return parent;
    }


    public void setParent(final AbstractModelElement parent) {
        this.parent = parent;
    }


    public Collection<AbstractModelElement> getChildren() {
        return Collections.emptyList();
    }


    @Override
    public int compareTo(final AbstractModelElement other) {
        return position.getOffset() - other.position.offset;
    }
}
