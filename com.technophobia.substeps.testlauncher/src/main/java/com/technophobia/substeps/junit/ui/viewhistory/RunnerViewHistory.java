package com.technophobia.substeps.junit.ui.viewhistory;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.internal.ui.viewsupport.ViewHistory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.junit.ui.SubstepsRunSessionManager;
import com.technophobia.substeps.junit.ui.SubstepsTestIcon;
import com.technophobia.substeps.model.structure.Result;
import com.technophobia.substeps.preferences.PreferencesConstants;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

@SuppressWarnings("restriction")
public class RunnerViewHistory extends ViewHistory<SubstepsRunSession> {

    private final Supplier<SubstepsRunSession> substepsRunSessionSupplier;
    private final Shell shell;
    private final SubstepsRunSessionManager sessionManager;
    private final Transformer<SubstepsIcon, ImageDescriptor> imageDescriptorTransformer;
    private final Action pasteAction;
    private final SubstepsIconProvider iconProvider;


    public RunnerViewHistory(final Supplier<SubstepsRunSession> substepsRunSessionSupplier, final Shell shell,
            final SubstepsRunSessionManager sessionManager,
            final Transformer<SubstepsIcon, ImageDescriptor> imageDescriptorTransformer,
            final SubstepsIconProvider iconProvider, final Action pasteAction) {
        this.substepsRunSessionSupplier = substepsRunSessionSupplier;
        this.shell = shell;
        this.sessionManager = sessionManager;
        this.imageDescriptorTransformer = imageDescriptorTransformer;
        this.iconProvider = iconProvider;
        this.pasteAction = pasteAction;
    }


    @Override
    public void configureHistoryListAction(final IAction action) {
        action.setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_history);
    }


    @Override
    public void configureHistoryDropDownAction(final IAction action) {
        action.setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_test_run_history);
        action.setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.HistoryListDisabled)); //$NON-NLS-1$
        action.setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.HistoryListEnabled)); //$NON-NLS-1$
        action.setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.HistoryListEnabled)); //$NON-NLS-1$
    }


    @Override
    public Action getClearAction() {
        return new ClearAction(this);
    }


    @Override
    public String getHistoryListDialogTitle() {
        return SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_test_runs;
    }


    @Override
    public String getHistoryListDialogMessage() {
        return SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_select_test_run;
    }


    @Override
    public Shell getShell() {
        return shell;
    }


    @Override
    public List<SubstepsRunSession> getHistoryEntries() {
        return FeatureRunnerPlugin.instance().getModel().getTestRunSessions();
    }


    @Override
    public SubstepsRunSession getCurrentEntry() {
        return substepsRunSessionSupplier.get();
    }


    @Override
    public void setActiveEntry(final SubstepsRunSession entry) {
        final SubstepsRunSession deactivatedSession = sessionManager.setActiveState(entry);
        if (deactivatedSession != null)
            deactivatedSession.swapOut();
    }


    @Override
    public void setHistoryEntries(final List<SubstepsRunSession> remainingEntries, final SubstepsRunSession activeEntry) {
        sessionManager.setActiveState(activeEntry);

        final List<SubstepsRunSession> substepsRunSessions = FeatureRunnerPlugin.instance().getModel()
                .getTestRunSessions();
        substepsRunSessions.removeAll(remainingEntries);
        for (final SubstepsRunSession substepsRunSession : substepsRunSessions) {
            FeatureRunnerPlugin.instance().getModel().removeTestRunSession(substepsRunSession);
        }
        for (final Iterator<SubstepsRunSession> iter = remainingEntries.iterator(); iter.hasNext();) {
            final SubstepsRunSession remaining = iter.next();
            remaining.swapOut();
        }
    }


    @Override
    public ImageDescriptor getImageDescriptor(final Object element) {
        final SubstepsRunSession session = (SubstepsRunSession) element;
        if (session.isStopped())
            return imageDescriptorTransformer.from(SubstepsTestIcon.Suite);

        if (session.isRunning())
            return imageDescriptorTransformer.from(SubstepsTestIcon.SuiteRunning);

        final Result result = session.getTestResult(true);
        if (result == Result.OK)
            return imageDescriptorTransformer.from(SubstepsTestIcon.SuiteOk);
        else if (result == Result.ERROR)
            return imageDescriptorTransformer.from(SubstepsTestIcon.SuiteError);
        else if (result == Result.FAILURE)
            return imageDescriptorTransformer.from(SubstepsTestIcon.SuiteFail);
        else
            return imageDescriptorTransformer.from(SubstepsTestIcon.Suite);
    }


    @Override
    public String getText(final SubstepsRunSession session) {
        final String testRunLabel = session.getTestRunName();
        if (session.getStartTime() <= 0) {
            return testRunLabel;
        }
        final String startTime = DateFormat.getDateTimeInstance().format(new Date(session.getStartTime()));
        return MessageFormat.format(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_testName_startTime,
                testRunLabel, startTime);
    }


    @Override
    public void addMenuEntries(final MenuManager manager) {
        manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, new ImportTestRunSessionAction(shell));
        manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, new ImportTestRunSessionFromURLAction(shell));
        manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, pasteAction);
        if (substepsRunSessionSupplier.get() != null)
            manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, new ExportTestRunSessionAction(shell,
                    substepsRunSessionSupplier.get()));
    }


    @Override
    public String getMaxEntriesMessage() {
        return SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_max_remembered;
    }


    @Override
    public int getMaxEntries() {
        return Platform.getPreferencesService().getInt(FeatureRunnerPlugin.PLUGIN_ID,
                PreferencesConstants.MAX_TEST_RUNS, 10, null);
    }


    @Override
    public void setMaxEntries(final int maxEntries) {
        InstanceScope.INSTANCE.getNode(FeatureRunnerPlugin.PLUGIN_ID).putInt(PreferencesConstants.MAX_TEST_RUNS,
                maxEntries);
    }
}
