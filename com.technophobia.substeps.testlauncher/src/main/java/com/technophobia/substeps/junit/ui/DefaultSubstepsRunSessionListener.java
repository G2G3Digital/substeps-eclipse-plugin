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