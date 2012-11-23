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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

public class TestSessionRunImporter {
    public static void importTestRunSession(@SuppressWarnings("unused") final String url) {
        try {
            PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
                @Override
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    throw new UnsupportedOperationException("Not yet implemented");
                    // SubstepsModel.importTestRunSession(url, monitor);
                }
            });
        } catch (final InterruptedException e) {
            // cancelled
        } catch (final InvocationTargetException e) {
            final CoreException ce = (CoreException) e.getCause();
            StatusManager.getManager().handle(ce.getStatus(), StatusManager.SHOW | StatusManager.LOG);
        }
    }
}
