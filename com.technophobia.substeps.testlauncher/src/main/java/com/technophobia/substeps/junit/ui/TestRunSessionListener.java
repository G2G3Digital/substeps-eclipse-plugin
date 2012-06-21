package com.technophobia.substeps.junit.ui;

import java.util.List;

import org.eclipse.jdt.internal.junit.BasicElementLabels;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.Messages;
import org.eclipse.jdt.internal.junit.model.ITestRunSessionListener;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.junit.ui.JUnitUIPreferencesConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPartSite;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.Notifier;

public class TestRunSessionListener implements ITestRunSessionListener {

    private final Supplier<Display> displaySupplier;
    private final Supplier<IWorkbenchPartSite> siteSupplier;
    private final Notifier<String> infoMessageNotifier;
    private final TestRunSessionManager sessionManager;


    public TestRunSessionListener(final Supplier<Display> displaySupplier,
            final Supplier<IWorkbenchPartSite> siteSupplier, final Notifier<String> infoMessageNotifier,
            final TestRunSessionManager sessionManager) {
        super();
        this.displaySupplier = displaySupplier;
        this.siteSupplier = siteSupplier;
        this.infoMessageNotifier = infoMessageNotifier;
        this.sessionManager = sessionManager;
    }


    @Override
    public void sessionAdded(final TestRunSession testRunSession) {
        displaySupplier.get().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (JUnitUIPreferencesConstants.getShowInAllViews()
                        || siteSupplier.get().getWorkbenchWindow() == JUnitPlugin.getActiveWorkbenchWindow()) {
                    if (infoMessageNotifier.currentValue() == null) {
                        final String testRunLabel = BasicElementLabels.getJavaElementName(testRunSession
                                .getTestRunName());
                        String msg;
                        if (testRunSession.getLaunch() != null) {
                            msg = Messages.format(JUnitMessages.TestRunnerViewPart_Launching,
                                    new Object[] { testRunLabel });
                        } else {
                            msg = testRunLabel;
                        }
                        infoMessageNotifier.notify(msg);
                    }

                    final TestRunSession deactivatedSession = sessionManager.setActiveState(testRunSession);
                    if (deactivatedSession != null)
                        deactivatedSession.swapOut();
                }
            }
        });
    }


    @Override
    public void sessionRemoved(final TestRunSession testRunSession) {
        displaySupplier.get().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (testRunSession.equals(sessionManager.get())) {
                    final List<TestRunSession> testRunSessions = JUnitCorePlugin.getModel().getTestRunSessions();
                    TestRunSession deactivatedSession;
                    if (!testRunSessions.isEmpty()) {
                        deactivatedSession = sessionManager.setActiveState(testRunSessions.get(0));
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