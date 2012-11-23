/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.editor.outline.model;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.text.Position;

import com.technophobia.substeps.supplier.Transformer;

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


    // TODO - potential optimisation here - currently we check every item if it
    // doesn't exist in the tree
    // - we could sort children by line number then do some binary search stuff
    public AbstractModelElement findItemAtLine(final int line,
            final Transformer<Position, Integer> offsetToLineNumberTransformer) {
        if (offsetToLineNumberTransformer.from(position).intValue() == line) {
            return this;
        }
        for (final AbstractModelElement child : getChildren()) {
            final AbstractModelElement itemAtLine = child.findItemAtLine(line, offsetToLineNumberTransformer);
            if (itemAtLine != null) {
                return itemAtLine;
            }
        }
        return null;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AbstractModelElement other = (AbstractModelElement) obj;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }
}
