package com.technophobia.substeps.junit.ui;

import org.eclipse.core.runtime.Platform;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.Resettable;
import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.component.FeatureViewer;
import com.technophobia.substeps.junit.ui.job.UpdateJobManager;
import com.technophobia.substeps.junit.ui.testsession.TestResultsView;
import com.technophobia.substeps.model.SubstepsSessionListener;
import com.technophobia.substeps.preferences.PreferencesConstants;
import com.technophobia.substeps.supplier.Supplier;

public class SubstepsRunSessionManager implements Supplier<SubstepsRunSession> {

    private SubstepsRunSession currentSession;
    private final FeatureViewer testViewer;
    private SubstepsSessionListener sessionListener;
    private final Supplier<Boolean> disposedSashFormChecker;
    private final UiUpdater tooltipUpdater;
    private final Resettable viewIconResetter;
    private final Resettable statusMessageResetter;
    private final Resettable failureTraceResetter;
    private final Notifier<String> infoMessageNotifier;

    private final Supplier<SubstepsSessionListener> testRunSessionListenerSupplier;

    private final boolean showOnErrorOnly = Platform.getPreferencesService().getBoolean(FeatureRunnerPlugin.PLUGIN_ID,
            PreferencesConstants.SHOW_ON_ERROR_ONLY, false, null);
    private final TestResultsView testResultsView;

    private final SubstepsActionManager actionManager;
    private final UpdateJobManager updateJobManager;


    public SubstepsRunSessionManager(final Supplier<Boolean> disposedSashFormChecker, final FeatureViewer testViewer,
            final UiUpdater tooltipUpdater, final Notifier<String> infoMessageNotifier,
            final Resettable viewIconResetter, final Resettable statusMessageResetter,
            final Resettable failureTraceResetter, final TestResultsView testResultsView,
            final SubstepsActionManager substepsActionManager, final UpdateJobManager updateJobManager,
            final Supplier<SubstepsSessionListener> testSessionListenerSupplier) {
        this.disposedSashFormChecker = disposedSashFormChecker;
        this.testViewer = testViewer;
        this.tooltipUpdater = tooltipUpdater;
        this.infoMessageNotifier = infoMessageNotifier;
        this.viewIconResetter = viewIconResetter;
        this.statusMessageResetter = statusMessageResetter;
        this.failureTraceResetter = failureTraceResetter;
        this.testResultsView = testResultsView;
        this.actionManager = substepsActionManager;
        this.updateJobManager = updateJobManager;
        testRunSessionListenerSupplier = testSessionListenerSupplier;
        this.currentSession = null;
    }


    public SubstepsRunSession setActiveState(final SubstepsRunSession substepsRunSession) {
        /*
         * - State: fTestRunSession fTestSessionListener Jobs
         * fTestViewer.processChangesInUI(); - UI: fCounterPanel fProgressBar
         * setContentDescription / fInfoMessage setTitleToolTip view icons
         * statusLine fFailureTrace
         * 
         * action enablement
         */
        if (currentSession == substepsRunSession)
            return null;

        deregisterTestSessionListener(true);

        final SubstepsRunSession deactivatedSession = currentSession;

        currentSession = substepsRunSession;

        testViewer.registerActiveSession(substepsRunSession);

        if (disposedSashFormChecker.get().booleanValue()) {
            updateJobManager.stopUpdateJobs();
            return deactivatedSession;
        }

        if (substepsRunSession == null) {
            tooltipUpdater.reset();
            viewIconResetter.reset();
            statusMessageResetter.reset();
            failureTraceResetter.reset();

            infoMessageNotifier.notify(" "); //$NON-NLS-1$
            updateJobManager.stopUpdateJobs();

            actionManager.setStopActionEnabled(false);
            actionManager.setRerunFailedFirstActionEnabled(false);
            actionManager.setRerunLastTestActionEnabled(false);

        } else {
            if (substepsRunSession.isStarting() || substepsRunSession.isRunning() || substepsRunSession.isKeptAlive()) {
                sessionListener = testRunSessionListenerSupplier.get();
                substepsRunSession.addTestSessionListener(sessionListener);
            }
            if (!substepsRunSession.isStarting() && !showOnErrorOnly)
                testResultsView.showTestResultsView();

            tooltipUpdater.doUpdate();

            statusMessageResetter.reset();
            failureTraceResetter.reset();
            infoMessageNotifier.notify(substepsRunSession.getTestRunName());

            updateRerunFailedFirstAction();
            actionManager.setRerunLastTestActionEnabled(substepsRunSession.getLaunch() != null);

            if (substepsRunSession.isRunning()) {
                updateJobManager.startUpdateJobs();

                actionManager.setStopActionEnabled(true);

            } else /* old or fresh session: don't want jobs at this stage */{
                updateJobManager.stopUpdateJobs();

                actionManager.setStopActionEnabled(substepsRunSession.isKeptAlive());
                testViewer.expandFirstLevel();
            }
        }
        return deactivatedSession;
    }


    @Override
    public SubstepsRunSession get() {
        return currentSession;
    }


    public void deregisterTestSessionListener(final boolean force) {
        if (currentSession != null && sessionListener != null && (force || !currentSession.isKeptAlive())) {
            currentSession.removeTestSessionListener(sessionListener);
            sessionListener = null;
        }
    }


    public void updateRerunFailedFirstAction() {
        final boolean state = currentSession.hasErrorsOrFailures() && currentSession.getLaunch() != null;
        actionManager.setRerunFailedFirstActionEnabled(state);
    }
}
