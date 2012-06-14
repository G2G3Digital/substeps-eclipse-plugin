package com.technophobia.eclipse.launcher.config;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;

public class FindExistingOrNewLaunchConfigFactory implements LaunchConfigurationFactory {

    private final ILaunchManager launchManager;
    private final String[] attributeNameMatchers;
    private final ExceptionReporter exceptionReporter;


    public FindExistingOrNewLaunchConfigFactory(final String[] attributeNameMatchers,
            final ILaunchManager launchManager, final ExceptionReporter exceptionReporter) {
        this.attributeNameMatchers = attributeNameMatchers;
        this.launchManager = launchManager;
        this.exceptionReporter = exceptionReporter;
    }


    @Override
    public ILaunchConfiguration create(final ILaunchConfigurationWorkingCopy workingCopy) {
        try {
            final ILaunchConfiguration existingLaunchConfig = findExistingOrNull(workingCopy);
            return existingLaunchConfig != null ? existingLaunchConfig : workingCopy.doSave();
        } catch (final CoreException ex) {
            exceptionReporter.report(ex);
            return null;
        }
    }


    private ILaunchConfiguration findExistingOrNull(final ILaunchConfigurationWorkingCopy workingCopy)
            throws CoreException {
        final ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(workingCopy.getType());
        for (final ILaunchConfiguration configuration : configurations) {
            if (attributesMatch(configuration, workingCopy)) {
                return configuration;
            }

        }
        return null;
    }


    private boolean attributesMatch(final ILaunchConfiguration configuration,
            final ILaunchConfigurationWorkingCopy workingCopy) {
        try {
            for (final String attributeName : attributeNameMatchers) {
                if (configuration.hasAttribute(attributeName)
                        && configuration.getAttribute(attributeName, "not-defined").equals(
                                workingCopy.getAttribute(attributeName, ""))) {
                    return true;
                }
            }
        } catch (final CoreException ex) {
            // no-op, just return false;
        }
        return false;
    }

}
