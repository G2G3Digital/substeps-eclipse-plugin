package com.technophobia.eclipse.launcher.config;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Locator;

public class FindExistingOrNewLaunchConfigFactory implements LaunchConfigurationFactory {

    private final ExceptionReporter exceptionReporter;
    private final Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> existingConfigLocator;


    public FindExistingOrNewLaunchConfigFactory(
            final Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> existingConfigLocator,
            final ExceptionReporter exceptionReporter) {
        this.existingConfigLocator = existingConfigLocator;
        this.exceptionReporter = exceptionReporter;
    }


    @Override
    public ILaunchConfiguration create(final ILaunchConfigurationWorkingCopy workingCopy) {
        try {
            final ILaunchConfiguration existingLaunchConfig = existingConfigLocator.one(workingCopy);
            return existingLaunchConfig != null ? existingLaunchConfig : workingCopy.doSave();
        } catch (final CoreException ex) {
            exceptionReporter.report(ex);
            return null;
        }
    }
}
