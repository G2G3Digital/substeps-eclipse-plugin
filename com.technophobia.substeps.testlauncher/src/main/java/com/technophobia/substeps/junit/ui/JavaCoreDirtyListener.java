package com.technophobia.substeps.junit.ui;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;

public class JavaCoreDirtyListener implements DirtyListener {

    private IElementChangedListener listener;


    @Override
    public void addNewDirtyListener() {
        if (listener == null) {
            listener = new DirtyFeatureListener();
            JavaCore.addElementChangedListener(listener);
        }
    }


    @Override
    public void removeDirtyListener() {
        if (listener != null) {
            JavaCore.removeElementChangedListener(listener);
            listener = null;
        }
    }

    private class DirtyFeatureListener implements IElementChangedListener {

        @Override
        public void elementChanged(final ElementChangedEvent event) {
            removeDirtyListener();
        }

    }
}
