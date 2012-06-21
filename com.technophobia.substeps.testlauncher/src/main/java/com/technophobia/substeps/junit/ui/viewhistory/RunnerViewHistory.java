package com.technophobia.substeps.junit.ui.viewhistory;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.internal.junit.BasicElementLabels;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.JUnitPreferencesConstants;
import org.eclipse.jdt.internal.junit.Messages;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.ViewHistory;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.transformer.Transformer;
import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.TestRunSessionManager;

public class RunnerViewHistory extends ViewHistory<TestRunSession> {

    private final Supplier<TestRunSession> testRunSessionSupplier;
    private final Shell shell;
    private final TestRunSessionManager sessionManager;
    private final Transformer<SubstepsIcon, ImageDescriptor> imageDescriptorTransformer;
    private final Action pasteAction;


    public RunnerViewHistory(final Supplier<TestRunSession> testRunSessionSupplier, final Shell shell,
            final TestRunSessionManager sessionManager,
            final Transformer<SubstepsIcon, ImageDescriptor> imageDescriptorTransformer, final Action pasteAction) {
        this.testRunSessionSupplier = testRunSessionSupplier;
        this.shell = shell;
        this.sessionManager = sessionManager;
        this.imageDescriptorTransformer = imageDescriptorTransformer;
        this.pasteAction = pasteAction;
    }


    @Override
    public void configureHistoryListAction(final IAction action) {
        action.setText(JUnitMessages.TestRunnerViewPart_history);
    }


    @Override
    public void configureHistoryDropDownAction(final IAction action) {
        action.setToolTipText(JUnitMessages.TestRunnerViewPart_test_run_history);
        JUnitPlugin.setLocalImageDescriptors(action, "history_list.gif"); //$NON-NLS-1$
    }


    @Override
    public Action getClearAction() {
        return new ClearAction(this);
    }


    @Override
    public String getHistoryListDialogTitle() {
        return JUnitMessages.TestRunnerViewPart_test_runs;
    }


    @Override
    public String getHistoryListDialogMessage() {
        return JUnitMessages.TestRunnerViewPart_select_test_run;
    }


    @Override
    public Shell getShell() {
        return shell;
    }


    @Override
    public List<TestRunSession> getHistoryEntries() {
        return JUnitCorePlugin.getModel().getTestRunSessions();
    }


    @Override
    public TestRunSession getCurrentEntry() {
        return testRunSessionSupplier.get();
    }


    @Override
    public void setActiveEntry(final TestRunSession entry) {
        final TestRunSession deactivatedSession = sessionManager.setActiveState(entry);
        if (deactivatedSession != null)
            deactivatedSession.swapOut();
    }


    @Override
    public void setHistoryEntries(final List<TestRunSession> remainingEntries, final TestRunSession activeEntry) {
        sessionManager.setActiveState(activeEntry);

        final List<org.eclipse.jdt.internal.junit.model.TestRunSession> testRunSessions = JUnitCorePlugin.getModel()
                .getTestRunSessions();
        testRunSessions.removeAll(remainingEntries);
        for (final org.eclipse.jdt.internal.junit.model.TestRunSession testRunSession : testRunSessions) {
            JUnitCorePlugin.getModel().removeTestRunSession(testRunSession);
        }
        for (final Iterator<TestRunSession> iter = remainingEntries.iterator(); iter.hasNext();) {
            final TestRunSession remaining = iter.next();
            remaining.swapOut();
        }
    }


    @Override
    public ImageDescriptor getImageDescriptor(final Object element) {
        final TestRunSession session = (TestRunSession) element;
        if (session.isStopped())
            return imageDescriptorTransformer.to(SubstepsIcon.Suite);

        if (session.isRunning())
            return imageDescriptorTransformer.to(SubstepsIcon.SuiteRunning);

        final Result result = session.getTestResult(true);
        if (result == Result.OK)
            return imageDescriptorTransformer.to(SubstepsIcon.SuiteOk);
        else if (result == Result.ERROR)
            return imageDescriptorTransformer.to(SubstepsIcon.SuiteError);
        else if (result == Result.FAILURE)
            return imageDescriptorTransformer.to(SubstepsIcon.SuiteFail);
        else
            return imageDescriptorTransformer.to(SubstepsIcon.Suite);
    }


    @Override
    public String getText(final TestRunSession session) {
        final String testRunLabel = BasicElementLabels.getJavaElementName(session.getTestRunName());
        if (session.getStartTime() <= 0) {
            return testRunLabel;
        } else {
            final String startTime = DateFormat.getDateTimeInstance().format(new Date(session.getStartTime()));
            return Messages.format(JUnitMessages.TestRunnerViewPart_testName_startTime, new Object[] { testRunLabel,
                    startTime });
        }
    }


    @Override
    public void addMenuEntries(final MenuManager manager) {
        manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, new ImportTestRunSessionAction(shell));
        manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, new ImportTestRunSessionFromURLAction(shell));
        manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, pasteAction);
        if (testRunSessionSupplier.get() != null)
            manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, new ExportTestRunSessionAction(shell,
                    testRunSessionSupplier.get()));
    }


    @Override
    public String getMaxEntriesMessage() {
        return JUnitMessages.TestRunnerViewPart_max_remembered;
    }


    @Override
    public int getMaxEntries() {
        return Platform.getPreferencesService().getInt(JUnitCorePlugin.CORE_PLUGIN_ID,
                JUnitPreferencesConstants.MAX_TEST_RUNS, 10, null);
    }


    @Override
    public void setMaxEntries(final int maxEntries) {
        InstanceScope.INSTANCE.getNode(JUnitCorePlugin.CORE_PLUGIN_ID).putInt(JUnitPreferencesConstants.MAX_TEST_RUNS,
                maxEntries);
    }
}
