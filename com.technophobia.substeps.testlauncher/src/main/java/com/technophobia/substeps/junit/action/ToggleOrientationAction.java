package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.view.ViewOrientation;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;

public class ToggleOrientationAction extends Action {
    private final ViewOrientation actionOrientation;
    private final Notifier<ViewOrientation> viewOrientationNotifier;


    public ToggleOrientationAction(final ViewOrientation orientation,
            final Notifier<ViewOrientation> viewOrientationNotifier, final SubstepsIconProvider iconProvider) {
        super("", AS_RADIO_BUTTON); //$NON-NLS-1$
        this.viewOrientationNotifier = viewOrientationNotifier;
        if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_HORIZONTAL)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_horizontal_label);
            setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.OrientationHorizontal)); //$NON-NLS-1$
        } else if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_VERTICAL)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_vertical_label);
            setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.OrientationVertical)); //$NON-NLS-1$
        } else if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_AUTOMATIC)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_automatic_label);
            setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.OrientationAutomatic)); //$NON-NLS-1$
        }
        actionOrientation = orientation;
        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(this, SubstepsHelpContextIds.RESULTS_VIEW_TOGGLE_ORIENTATION_ACTION);
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
