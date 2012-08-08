package com.technophobia.substeps.step.provider;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;

import com.technophobia.substeps.supplier.Callback1;

public class StepImplementationClassChangedListener implements IElementChangedListener {

    private final Callback1<IProject> onChangeCallback;


    public StepImplementationClassChangedListener(final Callback1<IProject> onChangeCallback) {
        this.onChangeCallback = onChangeCallback;
    }


    @Override
    public void elementChanged(final ElementChangedEvent event) {
        if (event.getType() == ElementChangedEvent.POST_CHANGE) {
            final IJavaElementDelta[] deltas = event.getDelta().getAffectedChildren();
            for (final IJavaElementDelta delta : deltas) {
                onChangeCallback.doCallback(((IJavaProject) delta.getElement()).getProject());
            }
            // onChangeCallback.doCallback();
        }
    }

}
