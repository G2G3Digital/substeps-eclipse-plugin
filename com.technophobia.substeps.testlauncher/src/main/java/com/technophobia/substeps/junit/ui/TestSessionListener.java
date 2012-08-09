package com.technophobia.substeps.junit.ui;

import java.text.MessageFormat;
import java.text.NumberFormat;

import com.technophobia.eclipse.transformer.Callback;
import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.Resettable;
import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.substeps.junit.ui.component.FeatureViewer;
import com.technophobia.substeps.junit.ui.job.UpdateJobManager;
import com.technophobia.substeps.junit.ui.testsession.TestResultsView;
import com.technophobia.substeps.model.SubstepsSessionListener;
import com.technophobia.substeps.model.structure.Status;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.model.structure.SubstepsTestLeafElement;
import com.technophobia.substeps.supplier.Supplier;

public class TestSessionListener implements SubstepsSessionListener {

    private final FeatureViewer testViewer;
    private final Notifier<Boolean> showOnErrorOnlyNotifier;
    private final UpdateJobManager updateJobManager;
    private final SubstepsActionManager actionManager;
    private final Notifier<String> infoMessageNotifier;
    private final SubstepsRunSessionManager sessionManager;
    private final Notifier<Runnable> postSyncNotifier;
    private final UiUpdater substepsFeatureUiUpdater;
    private final Supplier<Boolean> disposedChecker;
    private final Callback contentChangeCallback;
    private final Resettable viewIconResetter;
    private final Notifier<Boolean> autoScrollNotifier;
    private final TestResultsView testResultsView;
    private final FailureTrace failureTrace;
    private final DirtyListener dirtyListener;
    private final Supplier<TestRunStats> testRunStatsSupplier;


    public TestSessionListener(final FeatureViewer testViewer, final Notifier<Runnable> postSyncNotifier,
            final Notifier<String> infoMessageNotifier, final UiUpdater substepsFeatureUiUpdater,
            final Notifier<Boolean> showOnErrorOnlyNotifier, final UpdateJobManager updateJobManager,
            final SubstepsActionManager actionManager, final SubstepsRunSessionManager sessionManager,
            final Supplier<TestRunStats> testRunStatsSupplier, final Supplier<Boolean> disposedChecker,
            final Notifier<Boolean> autoScrollNotifier, final Callback contentChangeCallback,
            final Resettable viewIconResetter, final TestResultsView testResultsView, final FailureTrace failureTrace,
            final DirtyListener dirtyListener) {
        this.testViewer = testViewer;
        this.postSyncNotifier = postSyncNotifier;
        this.infoMessageNotifier = infoMessageNotifier;
        this.substepsFeatureUiUpdater = substepsFeatureUiUpdater;
        this.showOnErrorOnlyNotifier = showOnErrorOnlyNotifier;
        this.updateJobManager = updateJobManager;
        this.actionManager = actionManager;
        this.sessionManager = sessionManager;
        this.testRunStatsSupplier = testRunStatsSupplier;
        this.disposedChecker = disposedChecker;
        this.autoScrollNotifier = autoScrollNotifier;
        this.contentChangeCallback = contentChangeCallback;
        this.viewIconResetter = viewIconResetter;
        this.testResultsView = testResultsView;
        this.failureTrace = failureTrace;
        this.dirtyListener = dirtyListener;
    }


    @Override
    public void sessionStarted() {
        testViewer.registerViewersRefresh();

        updateJobManager.startUpdateJobs();

        actionManager.setStopActionEnabled(true);
        actionManager.setRerunLastTestActionEnabled(true);
    }


    @Override
    public void sessionEnded(final long elapsedTime) {
        sessionManager.deregisterTestSessionListener(false);

        testViewer.registerAutoScrollTarget(null);

        final Object[] keys = { elapsedTimeAsString(elapsedTime) };
        final String msg = MessageFormat.format(
                SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_finish, keys);
        infoMessageNotifier.notify(msg);

        postSyncNotifier.notify(new Runnable() {
            @Override
            public void run() {
                if (disposedChecker.get().booleanValue())
                    return;
                actionManager.setStopActionEnabled(sessionManager.get().isKeptAlive());
                sessionManager.updateRerunFailedFirstAction();
                substepsFeatureUiUpdater.doUpdate();

                if (testRunStatsSupplier.get().hasErrorsOrFailures()) {
                    testViewer.selectFirstFailure();
                }
                dirtyListener.addNewDirtyListener();
                contentChangeCallback.callback();
            }
        });
        updateJobManager.stopUpdateJobs();
    }


    @Override
    public void sessionStopped(final long elapsedTime) {
        sessionManager.deregisterTestSessionListener(false);

        testViewer.registerAutoScrollTarget(null);

        infoMessageNotifier.notify(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_stopped);
        handleStopped();
    }


    @Override
    public void sessionTerminated() {
        sessionManager.deregisterTestSessionListener(true);

        testViewer.registerAutoScrollTarget(null);

        infoMessageNotifier.notify(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_terminated);
        handleStopped();
    }


    @Override
    public void runningBegins() {
        if (!showOnErrorOnlyNotifier.currentValue().booleanValue()) {
            postShowTestResultsView();
        }
    }


    @Override
    public void testStarted(final SubstepsTestLeafElement testCaseElement) {
        testViewer.registerAutoScrollTarget(testCaseElement);
        testViewer.registerViewerUpdate(testCaseElement);

        final String className = testCaseElement.getClassName();
        final String method = testCaseElement.getTestMethodName();
        final String status = MessageFormat.format(
                SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_started, new Object[] { className,
                        method });
        infoMessageNotifier.notify(status);
    }


    @Override
    public void testFailed(final SubstepsTestElement testElement, final Status status, final String trace,
            final String expected, final String actual) {
        if (autoScrollNotifier.currentValue().booleanValue()) {
            testViewer.registerFailedForAutoScroll(testElement);
        }
        testViewer.registerViewerUpdate(testElement);

        // show the view on the first error only
        if (showOnErrorOnlyNotifier.currentValue().booleanValue()
                && (testRunStatsSupplier.get().getErrorCount() + testRunStatsSupplier.get().getFailureCount() == 1))
            postShowTestResultsView();

        // TODO:
        // [Bug 35590] JUnit window doesn't report errors from
        // junit.extensions.TestSetup [JUnit]
        // when a failure occurs in test setup then no test is running
        // to update the views we artificially signal the end of a test run
        // if (!fTestIsRunning) {
        // fTestIsRunning= false;
        // testEnded(testCaseElement);
        // }
    }


    @Override
    public void testEnded(final SubstepsTestLeafElement testCaseElement) {
        testViewer.registerViewerUpdate(testCaseElement);
    }


    @Override
    public void testReran(final SubstepsTestLeafElement testCaseElement, final Status status, final String trace,
            final String expectedResult, final String actualResult) {
        testViewer.registerViewerUpdate(testCaseElement); // TODO: autoExpand?

        postSyncNotifier.notify(new Runnable() {
            @Override
            public void run() {
                substepsFeatureUiUpdater.doUpdate();
            }
        });
        showFailure(testCaseElement);
    }


    @Override
    public void sessionLaunched(final SubstepsRunSession substepsRunSession) {
        // TODO Auto-generated method stub

    }


    private void showFailure(final SubstepsTestLeafElement testCaseElement) {
        postSyncNotifier.notify(new Runnable() {

            @Override
            public void run() {
                if (!disposedChecker.get().booleanValue())
                    failureTrace.showFailure(testCaseElement);
            }
        });
    }


    @Override
    public void testAdded(final SubstepsTestElement testElement) {
        testViewer.registerTestAdded(testElement);
    }


    @Override
    public boolean acceptsSwapToDisk() {
        return false;
    }


    private String elapsedTimeAsString(final long runTime) {
        return NumberFormat.getInstance().format((double) runTime / 1000);
    }


    private void handleStopped() {
        postSyncNotifier.notify(new Runnable() {
            @Override
            public void run() {
                if (disposedChecker.get().booleanValue())
                    return;
                viewIconResetter.reset();
                actionManager.setStopActionEnabled(false);
                sessionManager.updateRerunFailedFirstAction();
            }
        });
        updateJobManager.stopUpdateJobs();
    }


    private void postShowTestResultsView() {
        postSyncNotifier.notify(new Runnable() {

            @Override
            public void run() {
                if (!disposedChecker.get().booleanValue()) {
                    testResultsView.showTestResultsView();
                }
            }
        });
    }
}