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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;

import com.technophobia.substeps.supplier.Callback1;

public abstract class AbstractJavaProjectEventListener implements IElementChangedListener {

    private final Callback1<IProject> callback;
    private final int eventType;


    public AbstractJavaProjectEventListener(final int eventType, final Callback1<IProject> callback) {
        this.eventType = eventType;
        this.callback = callback;
    }


    @Override
    public void elementChanged(final ElementChangedEvent event) {
        if (event.getType() == ElementChangedEvent.POST_CHANGE) {
            final List<IJavaElementDelta> nodes = new ArrayList<IJavaElementDelta>();
            findNodesOfType(eventType, event.getDelta(), nodes);
            for (final IJavaElementDelta delta : nodes) {
                if (delta.getElement() instanceof IJavaProject) {
                    callback.doCallback(((IJavaProject) delta.getElement()).getProject());
                }
            }
        }
    }


    private void findNodesOfType(final int type, final IJavaElementDelta delta, final List<IJavaElementDelta> nodes) {
        if (delta.getKind() == type) {
            nodes.add(delta);
        }

        if ((delta.getFlags() & IJavaElementDelta.F_CHILDREN) == IJavaElementDelta.F_CHILDREN) {
            for (final IJavaElementDelta child : delta.getAffectedChildren()) {
                findNodesOfType(type, child, nodes);
            }
        }
    }
}
