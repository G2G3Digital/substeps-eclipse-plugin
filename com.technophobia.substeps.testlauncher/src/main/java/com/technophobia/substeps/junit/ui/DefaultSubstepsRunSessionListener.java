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
package com.technophobia.substeps.junit.ui;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.preferences.PreferencesConstants;
import com.technophobia.substeps.supplier.Supplier;

public class DefaultSubstepsRunSessionListener implements SubstepsRunSessionListener {

    private final Supplier<Display> displaySupplier;
    private final Supplier<IWorkbenchPartSite> siteSupplier;
    private final Notifier<String> infoMessageNotifier;
    private final SubstepsRunSessionManager sessionManager;


    public DefaultSubstepsRunSessionListener(final Supplier<Display> displaySupplier,
            final Supplier<IWorkbenchPartSite> siteSupplier, final Notifier<String> infoMessageNotifier,
            final SubstepsRunSessionManager sessionManager) {
        super();
        this.displaySupplier = displaySupplier;
        this.siteSupplier = siteSupplier;
        this.infoMessageNotifier = infoMessageNotifier;
        this.sessionManager = sessionManager;
    }


    @Override
    public void sessionAdded(final SubstepsRunSession substepsRunSession) {
        displaySupplier.get().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (PreferencesConstants.getShowInAllViews()
                        || siteSupplier.get().getWorkbenchWindow() == FeatureRunnerPlugin.instance()
                                .getActiveWorkbenchWindow()) {
                    if (infoMessageNotifier.currentValue() == null) {
                        final String substepsRunLabel = substepsRunSession.getTestRunName();
                        String msg;
                        if (substepsRunSession.getLaunch() != null) {
                            msg = MessageFormat.format(
                                    SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_Launching,
                                    substepsRunLabel);
                        } else {
                            msg = substepsRunLabel;
                        }
                        infoMessageNotifier.notify(msg);
                    }

                    final SubstepsRunSession deactivatedSession = sessionManager.setActiveState(substepsRunSession);
                    if (deactivatedSession != null)
                        deactivatedSession.swapOut();
                }
            }
        });
    }


    @Override
    public void sessionRemoved(final SubstepsRunSession substepsRunSession) {
        displaySupplier.get().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (substepsRunSession.equals(sessionManager.get())) {
                    final List<SubstepsRunSession> substepsRunSessions = FeatureRunnerPlugin.instance().getModel()
                            .getTestRunSessions();
                    SubstepsRunSession deactivatedSession;
                    if (!substepsRunSessions.isEmpty()) {
                        deactivatedSession = sessionManager.setActiveState(substepsRunSessions.get(0));
                    } else {
                        deactivatedSession = sessionManager.setActiveState(null);
                    }
                    if (deactivatedSession != null)
                        deactivatedSession.swapOut();
                }
            }
        });
    }
}
