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
