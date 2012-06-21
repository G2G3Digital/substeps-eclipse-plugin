package com.technophobia.substeps.junit.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.internal.junit.Messages;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.Notifier;

public class TestRelauncher implements Runnable {

    private final Supplier<TestRunSession> testRunSession;
    private final Shell shell;
    private final Notifier<String> infoMessageNotifier;


    public TestRelauncher(final Supplier<TestRunSession> testRunSession, final Shell shell,
            final Notifier<String> infoMessageNotifier) {
        this.testRunSession = testRunSession;
        this.shell = shell;
        this.infoMessageNotifier = infoMessageNotifier;
    }


    /**
     * Stops the currently running test and shuts down the RemoteTestRunner
     */
    @Override
    public void run() {
        stopIfCurrentlyRunning();

        final ILaunch launch = testRunSession.get().getLaunch();
        if (launch == null)
            return;
        final ILaunchConfiguration launchConfiguration = launch.getLaunchConfiguration();
        if (launchConfiguration == null)
            return;

        final ILaunchConfiguration configuration = prepareLaunchConfigForRelaunch(launchConfiguration);
        relaunch(configuration, launch.getLaunchMode());
    }


    private void stopIfCurrentlyRunning() {
        if (testRunSession.get().isKeptAlive()) {
            // prompt for terminating the existing run
            if (MessageDialog.openQuestion(shell,
                    SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_terminate_title,
                    SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_terminate_message)) {
                stopTest(); // TODO: wait for terminatio
            }
        }
    }


    private void stopTest() {
        if (testRunSession != null) {
            if (testRunSession.get().isRunning()) {
                infoMessageNotifier.notify(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_stopping);
            }
            testRunSession.get().stopTestRun();
        }
    }


    protected ILaunchConfiguration prepareLaunchConfigForRelaunch(final ILaunchConfiguration configuration) {
        try {
            final String attribute = configuration.getAttribute(JUnitLaunchConfigurationConstants.ATTR_FAILURES_NAMES,
                    ""); //$NON-NLS-1$
            if (attribute.length() != 0) {
                final String configName = Messages.format(
                        SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_configName, configuration.getName());
                final ILaunchConfigurationWorkingCopy tmp = configuration.copy(configName);
                tmp.setAttribute(JUnitLaunchConfigurationConstants.ATTR_FAILURES_NAMES, ""); //$NON-NLS-1$
                return tmp;
            }
        } catch (final CoreException e) {
            // fall through
        }
        return configuration;
    }


    private void relaunch(final ILaunchConfiguration configuration, final String launchMode) {
        DebugUITools.launch(configuration, launchMode);
    }
}
