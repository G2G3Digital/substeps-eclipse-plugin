package com.technophobia.substeps.junit.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.internal.junit.Messages;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.model.TestElement;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.Notifier;

public class FailedTestFirstTestRelauncher extends TestRelauncher {

    private final Shell shell;
    final Supplier<TestRunSession> testRunSession;


    public FailedTestFirstTestRelauncher(final Supplier<TestRunSession> testRunSession, final Shell shell,
            final Notifier<String> infoMessageNotifier) {
        super(testRunSession, shell, infoMessageNotifier);
        this.testRunSession = testRunSession;
        this.shell = shell;
    }


    @Override
    protected ILaunchConfiguration prepareLaunchConfigForRelaunch(final ILaunchConfiguration launchConfiguration) {
        try {
            final String oldName = launchConfiguration.getName();
            final String oldFailuresFilename = launchConfiguration.getAttribute(
                    JUnitLaunchConfigurationConstants.ATTR_FAILURES_NAMES, (String) null);
            String configName;
            if (oldFailuresFilename != null) {
                configName = oldName;
            } else {
                configName = Messages.format(
                        SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_rerunFailedFirstLaunchConfigName,
                        oldName);
            }
            final ILaunchConfigurationWorkingCopy tmp = launchConfiguration.copy(configName);
            tmp.setAttribute(JUnitLaunchConfigurationConstants.ATTR_FAILURES_NAMES, createFailureNamesFile());
            return tmp;
        } catch (final CoreException ex) {
            ErrorDialog.openError(shell, SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_error_cannotrerun,
                    ex.getMessage(), ex.getStatus());
            return null;
        }
    }


    private String createFailureNamesFile() throws CoreException {
        try {
            final File file = File.createTempFile("testFailures", ".txt"); //$NON-NLS-1$ //$NON-NLS-2$
            file.deleteOnExit();
            final TestElement[] failures = testRunSession.get().getAllFailedTestElements();
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")); //$NON-NLS-1$
                for (final TestElement testElement : failures) {
                    bw.write(testElement.getTestName());
                    bw.newLine();
                }
            } finally {
                if (bw != null) {
                    bw.close();
                }
            }
            return file.getAbsolutePath();
        } catch (final IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, JUnitPlugin.PLUGIN_ID, IStatus.ERROR, "", e)); //$NON-NLS-1$
        }
    }
}
