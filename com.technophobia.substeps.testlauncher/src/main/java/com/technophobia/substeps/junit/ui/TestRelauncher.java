package com.technophobia.substeps.junit.ui;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;
import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.supplier.Supplier;

public class TestRelauncher implements Runnable {

    private final Supplier<SubstepsRunSession> substepsRunSession;
    private final Shell shell;
    private final Notifier<String> infoMessageNotifier;


    public TestRelauncher(final Supplier<SubstepsRunSession> substepsRunSession, final Shell shell,
            final Notifier<String> infoMessageNotifier) {
        this.substepsRunSession = substepsRunSession;
        this.shell = shell;
        this.infoMessageNotifier = infoMessageNotifier;
    }


    /**
     * Stops the currently running test and shuts down the RemoteTestRunner
     */
    @Override
    public void run() {
        stopIfCurrentlyRunning();

        final ILaunch launch = substepsRunSession.get().getLaunch();
        if (launch == null)
            return;
        final ILaunchConfiguration launchConfiguration = launch.getLaunchConfiguration();
        if (launchConfiguration == null)
            return;

        final ILaunchConfiguration configuration = prepareLaunchConfigForRelaunch(launchConfiguration);
        relaunch(configuration, launch.getLaunchMode());
    }


    private void stopIfCurrentlyRunning() {
        if (substepsRunSession.get().isKeptAlive()) {
            // prompt for terminating the existing run
            if (MessageDialog.openQuestion(shell,
                    SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_terminate_title,
                    SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_terminate_message)) {
                stopTest(); // TODO: wait for terminatio
            }
        }
    }


    private void stopTest() {
        if (substepsRunSession != null) {
            if (substepsRunSession.get().isRunning()) {
                infoMessageNotifier.notify(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_stopping);
            }
            substepsRunSession.get().stopTestRun();
        }
    }


    protected ILaunchConfiguration prepareLaunchConfigForRelaunch(final ILaunchConfiguration configuration) {
        try {
            final String attribute = configuration.getAttribute(
                    SubstepsLaunchConfigurationConstants.ATTR_FAILURES_NAMES, ""); //$NON-NLS-1$
            if (attribute.length() != 0) {
                final String configName = MessageFormat.format(
                        SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_configName, configuration.getName());
                final ILaunchConfigurationWorkingCopy tmp = configuration.copy(configName);
                tmp.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_FAILURES_NAMES, ""); //$NON-NLS-1$
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
