package com.technophobia.substeps.step.provider;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

public class StepImplementationClassChangeListenerOld implements IResourceChangeListener {

    private static final int[] VALID_EVENT_KINDS = new int[] { IResourceDelta.CHANGED, IResourceDelta.ADDED,
            IResourceDelta.REMOVED };


    @Override
    public void resourceChanged(final IResourceChangeEvent event) {
        try {
            event.getDelta().accept(new IResourceDeltaVisitor() {

                @Override
                public boolean visit(final IResourceDelta delta) throws CoreException {
                    if (isValidKind(delta)) {
                        final IResource resource = delta.getResource();
                        if (resource instanceof IFolder || resource instanceof IWorkspaceRoot
                                || resource instanceof IProject) {
                            return true;
                        }

                        if (resource instanceof IFile) {
                            handleFile((IFile) resource, delta.getKind());
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (final CoreException ex) {
            ex.printStackTrace();
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


    private void handleFile(final IFile file, final int kind) {
        if ("java".equals(file.getFileExtension())) {
            System.out.println("Found java file" + file);
            final IJavaElement javaFile = JavaCore.create(file);
            javaFile.getPrimaryElement();
        }
    }
}
