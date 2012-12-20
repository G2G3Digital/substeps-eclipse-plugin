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
package com.technophobia.substeps.editor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class UpdateOutlineOnSelectionChangedListener implements ISelectionListener {

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (part instanceof FeatureEditor) {
            ((IContentOutlinePage) ((FeatureEditor) part).getAdapter(IContentOutlinePage.class))
                    .setSelection(selection);
            // ((TextSelection)selection).getStartLine();
        }
        System.out.println("Source " + part + ", " + selection);
    }

}
