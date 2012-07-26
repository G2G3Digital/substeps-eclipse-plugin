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
