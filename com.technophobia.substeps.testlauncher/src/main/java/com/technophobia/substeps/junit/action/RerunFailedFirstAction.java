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

import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.TestRelauncher;

public class RerunFailedFirstAction extends Action {

    private final TestRelauncher testRelauncher;


    public RerunFailedFirstAction(final String actionDefinitionId, final TestRelauncher testRelauncher,
            final SubstepsIconProvider iconProvider) {
        this.testRelauncher = testRelauncher;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunfailuresaction_label);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunfailuresaction_label);
        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchFailedDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchFailedEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.RelaunchFailedEnabled)); //$NON-NLS-1$
        setEnabled(false);
        setActionDefinitionId(actionDefinitionId);
    }


    @Override
    public void run() {
        testRelauncher.run();
    }

}
