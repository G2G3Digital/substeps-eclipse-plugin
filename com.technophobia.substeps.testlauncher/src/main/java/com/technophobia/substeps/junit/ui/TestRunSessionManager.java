package com.technophobia.substeps.junit.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.junit.BasicElementLabels;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.JUnitPreferencesConstants;
import org.eclipse.jdt.internal.junit.model.ITestSessionListener;
import org.eclipse.jdt.internal.junit.model.TestRunSession;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.Resettable;
import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.substeps.junit.ui.job.UpdateJobManager;
import com.technophobia.substeps.junit.ui.testsession.TestResultsView;

public class TestRunSessionManager implements Supplier<TestRunSession> {

    private TestRunSession currentSession;
    private final FeatureViewer testViewer;
    private final Supplier<TestRunStats> testRunStats;
    private ITestSessionListener sessionListener;
    private final Supplier<Boolean> disposedSashFormChecker;
    private final UiUpdater tooltipUpdater;
    private final Resettable viewIconResetter;
    private final Resettable statusMessageResetter;
    private final Resettable failureTraceResetter;
    private final Notifier<String> infoMessageNotifier;

    private final Supplier<ITestSessionListener> testRunSessionListenerSupplier;

    private final boolean showOnErrorOnly = Platform.getPreferencesService().getBoolean(JUnitCorePlugin.CORE_PLUGIN_ID,
            JUnitPreferencesConstants.SHOW_ON_ERROR_ONLY, false, null);
    private final TestResultsView testResultsView;

    private final SubstepsActionManager actionManager;
    private final UpdateJobManager updateJobManager;


    @SuppressWarnings("restriction")
    public TestRunSessionManager(final Supplier<Boolean> disposedSashFormChecker,
            final Supplier<TestRunStats> testRunStats, final FeatureViewer testViewer, final UiUpdater tooltipUpdater,
            final Notifier<String> infoMessageNotifier, final Resettable viewIconResetter,
            final Resettable statusMessageResetter, final Resettable failureTraceResetter,
            final TestResultsView testResultsView, final SubstepsActionManager substepsActionManager,
            final UpdateJobManager updateJobManager, final Supplier<ITestSessionListener> testSessionListenerSupplier) {
        this.disposedSashFormChecker = disposedSashFormChecker;
        this.testRunStats = testRunStats;
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


    public TestRunSession setActiveState(final TestRunSession testRunSession) {
        /*
         * - State: fTestRunSession fTestSessionListener Jobs
         * fTestViewer.processChangesInUI(); - UI: fCounterPanel fProgressBar
         * setContentDescription / fInfoMessage setTitleToolTip view icons
         * statusLine fFailureTrace
         * 
         * action enablement
         */
        if (currentSession == testRunSession)
            return null;

        deregisterTestSessionListener(true);

        final TestRunSession deactivatedSession = currentSession;

        currentSession = testRunSession;

        testViewer.registerActiveSession(testRunSession);

        if (disposedSashFormChecker.get()) {
            updateJobManager.stopUpdateJobs();
            return deactivatedSession;
        }

        if (testRunSession == null) {
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
            if (testRunSession.isStarting() || testRunSession.isRunning() || testRunSession.isKeptAlive()) {
                sessionListener = testRunSessionListenerSupplier.get();
                testRunSession.addTestSessionListener(sessionListener);
            }
            if (!testRunSession.isStarting() && !showOnErrorOnly)
                testResultsView.showTestResultsView();

            tooltipUpdater.doUpdate();

            statusMessageResetter.reset();
            failureTraceResetter.reset();
            infoMessageNotifier.notify(BasicElementLabels.getJavaElementName(testRunSession.getTestRunName()));

            updateRerunFailedFirstAction();
            actionManager.setRerunLastTestActionEnabled(testRunSession.getLaunch() != null);

            if (testRunSession.isRunning()) {
                updateJobManager.startUpdateJobs();

                actionManager.setStopActionEnabled(true);

            } else /* old or fresh session: don't want jobs at this stage */{
                updateJobManager.stopUpdateJobs();

                actionManager.setStopActionEnabled(testRunSession.isKeptAlive());
                testViewer.expandFirstLevel();
            }
        }
        return deactivatedSession;
    }


    @Override
    public TestRunSession get() {
        return currentSession;
    }


    public void deregisterTestSessionListener(final boolean force) {
        if (currentSession != null && sessionListener != null && (force || !currentSession.isKeptAlive())) {
            currentSession.removeTestSessionListener(sessionListener);
            sessionListener = null;
        }
    }


    public void updateRerunFailedFirstAction() {
        final boolean state = testRunStats.get().hasErrorsOrFailures() && currentSession.getLaunch() != null;
        actionManager.setRerunFailedFirstActionEnabled(state);
    }
}
