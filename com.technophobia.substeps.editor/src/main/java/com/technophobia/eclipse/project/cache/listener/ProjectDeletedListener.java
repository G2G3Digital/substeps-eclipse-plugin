package com.technophobia.eclipse.project.cache.listener;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElementDelta;

import com.technophobia.substeps.supplier.Callback1;

public class ProjectDeletedListener extends AbstractJavaProjectEventListener {

    public ProjectDeletedListener(final Callback1<IProject> callback) {
        super(IJavaElementDelta.REMOVED, callback);
    }

}
