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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;

import com.technophobia.substeps.supplier.Callback1;

public class StepImplementationClassChangedListener implements IElementChangedListener {

    private final Callback1<IProject> onChangeCallback;


    public StepImplementationClassChangedListener(final Callback1<IProject> onChangeCallback) {
        this.onChangeCallback = onChangeCallback;
    }


    @Override
    public void elementChanged(final ElementChangedEvent event) {
        if (event.getType() == ElementChangedEvent.POST_RECONCILE && event.getDelta().getResourceDeltas() != null) {
            final Collection<IResource> items = new ArrayList<IResource>();
            for (final IResourceDelta resourceDelta : event.getDelta().getResourceDeltas()) {
                changedLeafItems(resourceDelta, items);
            }

            for (final IResource resource : items) {
                final String t = resource.getName();
            }
            // onChangeCallback.doCallback();
        }
    }


    private void changedLeafItems(final IResourceDelta resourceDelta, final Collection<IResource> leafItems) {
        if (resourceDelta.getAffectedChildren().length == 0) {
            leafItems.add(resourceDelta.getResource());
        } else {
            for (final IResourceDelta child : resourceDelta.getAffectedChildren()) {
                changedLeafItems(child, leafItems);
            }
        }
    }
}
