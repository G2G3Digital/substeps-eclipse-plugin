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
