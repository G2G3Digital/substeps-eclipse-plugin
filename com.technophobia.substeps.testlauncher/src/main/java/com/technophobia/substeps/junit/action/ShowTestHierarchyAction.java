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
import org.eclipse.jface.action.IAction;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.view.ViewLayout;
import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;

public class ShowTestHierarchyAction extends Action {

    private final Notifier<ViewLayout> layoutModeNotifier;


    public ShowTestHierarchyAction(final Notifier<ViewLayout> layoutModeNotifier,
            final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_hierarchical_layout, IAction.AS_CHECK_BOX);
        this.layoutModeNotifier = layoutModeNotifier;
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.HierarchicalLayout)); //$NON-NLS-1$
    }


    @Override
    public void run() {
        final ViewLayout mode = isChecked() ? ViewLayout.HIERARCHICAL : ViewLayout.FLAT;
        layoutModeNotifier.notify(mode);
    }
}
