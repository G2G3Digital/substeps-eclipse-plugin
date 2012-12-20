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

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.preferences.PreferencesConstants;

public class ActivateOnErrorAction extends Action {
    private final Notifier<Boolean> showOnErrorOnlyNotifier;


    public ActivateOnErrorAction(final boolean initialValue, final Notifier<Boolean> showOnErrorOnlyNotifier) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_activate_on_failure_only, IAction.AS_CHECK_BOX);
        this.showOnErrorOnlyNotifier = showOnErrorOnlyNotifier;
        //setImageDescriptor(JUnitPlugin.getImageDescriptor("obj16/failures.gif")); //$NON-NLS-1$

        update(initialValue);
    }


    public void update(final boolean value) {
        setChecked(value);
    }


    @Override
    public void run() {
        final boolean checked = isChecked();
        showOnErrorOnlyNotifier.notify(Boolean.valueOf(checked));
        InstanceScope.INSTANCE.getNode(FeatureRunnerPlugin.PLUGIN_ID).putBoolean(
                PreferencesConstants.SHOW_ON_ERROR_ONLY, checked);
    }
}
