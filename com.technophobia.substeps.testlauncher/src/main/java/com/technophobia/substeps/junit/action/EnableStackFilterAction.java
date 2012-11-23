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

import com.technophobia.eclipse.ui.Refreshable;
import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;
import com.technophobia.substeps.preferences.PreferencesConstants;

public class EnableStackFilterAction extends Action {

    private final Refreshable view;


    public EnableStackFilterAction(final Refreshable view, final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.EnableStackFilterAction_action_label);
        setDescription(SubstepsFeatureMessages.EnableStackFilterAction_action_description);
        setToolTipText(SubstepsFeatureMessages.EnableStackFilterAction_action_tooltip);

        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StackFilterDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StackFilterEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StackFilterEnabled)); //$NON-NLS-1$
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, SubstepsHelpContextIds.ENABLEFILTER_ACTION);

        this.view = view;
        setChecked(PreferencesConstants.getFilterStack());
    }


    /*
     * @see Action#actionPerformed
     */
    @Override
    public void run() {
        PreferencesConstants.setFilterStack(isChecked());
        view.refresh();
    }
}
