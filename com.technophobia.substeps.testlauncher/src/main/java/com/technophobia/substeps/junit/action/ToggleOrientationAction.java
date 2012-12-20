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
package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.view.ViewOrientation;
import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
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
            setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.OrientationHorizontal)); //$NON-NLS-1$
        } else if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_VERTICAL)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_vertical_label);
            setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.OrientationVertical)); //$NON-NLS-1$
        } else if (orientation.equals(ViewOrientation.VIEW_ORIENTATION_AUTOMATIC)) {
            setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_toggle_automatic_label);
            setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.OrientationAutomatic)); //$NON-NLS-1$
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
