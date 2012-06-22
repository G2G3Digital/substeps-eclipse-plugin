package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.IJUnitHelpContextIds;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.view.ViewOrientation;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class ToggleOrientationAction extends Action {
    private final ViewOrientation actionOrientation;
    private final Notifier<ViewOrientation> viewOrientationNotifier;


    public ToggleOrientationAction(final ViewOrientation orientation,
            final Notifier<ViewOrientation> viewOrientationNotifier) {
        super("", AS_RADIO_BUTTON); //$NON-NLS-1$
        this.viewOrientationNotifier = viewOrientationNotifier;
        if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_HORIZONTAL)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_horizontal_label);
            setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/th_horizontal.gif")); //$NON-NLS-1$
        } else if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_VERTICAL)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_vertical_label);
            setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/th_vertical.gif")); //$NON-NLS-1$
        } else if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_AUTOMATIC)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_automatic_label);
            setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/th_automatic.gif")); //$NON-NLS-1$
        }
        actionOrientation = orientation;
        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(this, IJUnitHelpContextIds.RESULTS_VIEW_TOGGLE_ORIENTATION_ACTION);
    }


    public int getOrientation() {
        return actionOrientation.value();
    }


    @Override
    public void run() {
        if (isChecked()) {
            viewOrientationNotifier.notify(actionOrientation);
        }
    }
}
