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
