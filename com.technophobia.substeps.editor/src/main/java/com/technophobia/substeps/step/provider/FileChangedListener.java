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
package com.technophobia.substeps.step.provider;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Callback1;

public class FileChangedListener implements IResourceChangeListener {

    private static final int[] VALID_EVENT_KINDS = new int[] { IResourceDelta.CHANGED, IResourceDelta.ADDED,
            IResourceDelta.REMOVED };

    private final String fileExtension;
    private final Callback1<IFile> fileChangedCallback;


    public FileChangedListener(final String fileExtension, final Callback1<IFile> fileChangedCallback) {
        this.fileExtension = fileExtension;
        this.fileChangedCallback = fileChangedCallback;
    }


    @Override
    public void resourceChanged(final IResourceChangeEvent event) {
        try {
            event.getDelta().accept(new IResourceDeltaVisitor() {

                @Override
                public boolean visit(final IResourceDelta delta) throws CoreException {
                    if (isValidKind(delta)) {
                        final IResource resource = delta.getResource();
                        if (resource instanceof IContainer) {
                            return true;
                        }

                        if (resource instanceof IFile) {
                            handleFile((IFile) resource);
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (final CoreException ex) {
            FeatureEditorPlugin.instance().warn("Could not handle file change for event " + event);
        }
        System.out.println(event);
    }


    private boolean isValidKind(final IResourceDelta delta) {
        final int kind = delta.getKind();
        for (final int validEventKind : VALID_EVENT_KINDS) {
            if (kind == validEventKind) {
                return true;
            }
        }
        return false;
    }


    private void handleFile(final IFile file) {
        if (fileExtension.equals(file.getFileExtension())) {
            fileChangedCallback.doCallback(file);
        }
    }
}
