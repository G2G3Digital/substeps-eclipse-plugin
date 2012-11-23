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

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;
import com.technophobia.substeps.model.structure.SubstepsTestParentElement;
import com.technophobia.substeps.model.structure.SubstepsTestRootElement;

public class TestSessionTableContentProvider implements IStructuredContentProvider {

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        // No-op
    }


    @Override
    public Object[] getElements(final Object inputElement) {
        final ArrayList<SubstepsTestElement> all = new ArrayList<SubstepsTestElement>();
        addAll(all, (SubstepsTestRootElement) inputElement);
        return all.toArray();
    }


    private void addAll(final ArrayList<SubstepsTestElement> all, final SubstepsTestParentElement suite) {
        final SubstepsTestElement[] children = suite.getChildren();
        for (final SubstepsTestElement element : children) {
            if (element instanceof SubstepsTestParentElement) {
                if (((SubstepsTestParentElement) element).getStatus().isErrorOrFailure())
                    all.add(element); // add failed suite to flat list too
                addAll(all, (SubstepsTestParentElement) element);
            } else if (element instanceof SubstepsTestLeafElement) {
                all.add(element);
            }
        }
    }


    @Override
    public void dispose() {
        // No-op
    }
}
