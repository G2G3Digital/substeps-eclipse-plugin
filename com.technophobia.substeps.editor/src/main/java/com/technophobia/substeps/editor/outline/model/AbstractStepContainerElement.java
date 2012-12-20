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
