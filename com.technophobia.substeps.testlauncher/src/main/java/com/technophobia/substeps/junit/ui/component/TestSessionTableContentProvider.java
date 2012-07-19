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
    }
}
