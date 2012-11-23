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

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.supplier.Supplier;

public class StopTestAction extends Action {
    private final Supplier<SubstepsRunSession> substepsRunSessionSupplier;
    private final Notifier<String> infoMessageUpdater;


    public StopTestAction(final Supplier<SubstepsRunSession> substepsRunSessionSupplier,
            final Notifier<String> infoMessageUpdater, final SubstepsIconProvider iconProvider) {
        this.substepsRunSessionSupplier = substepsRunSessionSupplier;
        this.infoMessageUpdater = infoMessageUpdater;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_stopaction_text);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_stopaction_text);
        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StopDisbled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StopEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StopEnabled)); //$NON-NLS-1$
    }


    @Override
    public void run() {
        stopTest();
        setEnabled(false);
    }


    private void stopTest() {
        final SubstepsRunSession substepsRunSession = substepsRunSessionSupplier.get();
        if (substepsRunSession != null) {
            if (substepsRunSession.isRunning()) {
                infoMessageUpdater.notify(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_stopping);
            }
            substepsRunSession.stopTestRun();
        }
    }
}
