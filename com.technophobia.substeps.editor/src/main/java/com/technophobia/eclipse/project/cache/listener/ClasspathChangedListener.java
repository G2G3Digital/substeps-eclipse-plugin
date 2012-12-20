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
package com.technophobia.eclipse.project.cache.listener;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Callback1;

public class ClasspathChangedListener implements IElementChangedListener {

    private static final int FLAGS = IJavaElementDelta.F_ADDED_TO_CLASSPATH
            | IJavaElementDelta.F_REMOVED_FROM_CLASSPATH | IJavaElementDelta.F_CLASSPATH_CHANGED;

    /**
     * TODO: ProjectEventType no longer aware of build flags; this class - only
     * for classpath changed events new listener class for project classes -
     * listen for resource deltas that cover the java project output folders -
     * if found, look for classes. if there are any, update cache
     * 
     */

    private final Callback1<IProject> projectChangedNotifier;


    public ClasspathChangedListener(final Callback1<IProject> projectChangedNotifier) {
        this.projectChangedNotifier = projectChangedNotifier;
    }


    @Override
    public void elementChanged(final ElementChangedEvent event) {
        if (event.getType() == ElementChangedEvent.POST_CHANGE) {
            final IJavaElementDelta delta = event.getDelta();

            if (isDeltaChangeMatchForEvent(delta, FLAGS)) {
                try {
                    for (final IJavaProject javaProject : delta.getElement().getJavaModel().getJavaProjects()) {
                        final IProject project = javaProject.getProject();
                        FeatureEditorPlugin.instance().info("Classpath has changed for project " + project);
                        projectChangedNotifier.doCallback(project);
                    }
                } catch (final JavaModelException ex) {
                    FeatureEditorPlugin.instance().error(
                            "Could not update caches for classpath change with event " + event, ex);
                }
            }
        }
    }


    private boolean isDeltaChangeMatchForEvent(final IJavaElementDelta delta, final int flags) {
        if (hasMatchingFlag(delta.getFlags(), flags)) {
            return true;
        }
        for (final IJavaElementDelta child : delta.getAffectedChildren()) {
            if (isDeltaChangeMatchForEvent(child, flags)) {
                return true;
            }
        }
        return false;
    }


    protected boolean hasMatchingFlag(final int flags1, final int flags2) {
        return (flags1 & flags2) != 0;
    }
}
