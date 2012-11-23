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
package com.technophobia.substeps.junit.ui.component;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestParentElement;
import com.technophobia.substeps.model.structure.SubstepsTestRootElement;

public class TestSessionTreeContentProvider implements ITreeContentProvider {

    private final Object[] NO_CHILDREN = new Object[0];


    @Override
    public void dispose() {
        // No-op
    }


    @Override
    public Object[] getChildren(final Object parentElement) {
        if (parentElement instanceof SubstepsTestParentElement)
            return ((SubstepsTestParentElement) parentElement).getChildren();
        return NO_CHILDREN;
    }


    @Override
    public Object[] getElements(final Object inputElement) {
        return ((SubstepsTestRootElement) inputElement).getChildren();
    }


    @Override
    public Object getParent(final Object element) {
        return ((SubstepsTestElement) element).getParent();
    }


    @Override
    public boolean hasChildren(final Object element) {
        if (element instanceof SubstepsTestParentElement)
            return ((SubstepsTestParentElement) element).getChildren().length != 0;
        return false;
    }


    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        // No-op
    }
}
