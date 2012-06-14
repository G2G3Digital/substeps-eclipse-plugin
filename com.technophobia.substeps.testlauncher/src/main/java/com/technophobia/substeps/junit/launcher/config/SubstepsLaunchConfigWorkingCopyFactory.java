package com.technophobia.substeps.junit.launcher.config;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import com.technophobia.eclipse.launcher.config.LaunchConfigurationWorkingCopyFactory;
import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Decorator;

public class SubstepsLaunchConfigWorkingCopyFactory implements LaunchConfigurationWorkingCopyFactory {

    private static final String LAUNCH_CONFIG_ID = "com.technophobia.substeps.junit.launchconfig";

    private final ILaunchManager launchManager;

    private final Collection<Decorator<ILaunchConfigurationWorkingCopy, IResource>> workingCopyDecorators;

    private final ExceptionReporter exceptionReporter;


    public SubstepsLaunchConfigWorkingCopyFactory(final ILaunchManager launchManager,
            final Collection<Decorator<ILaunchConfigurationWorkingCopy, IResource>> workingCopyDecorators,
            final ExceptionReporter exceptionReporter) {
        this.launchManager = launchManager;
        this.workingCopyDecorators = workingCopyDecorators;
        this.exceptionReporter = exceptionReporter;
    }


    @Override
    public ILaunchConfigurationWorkingCopy create(final String name, final IResource resource) {
        final ILaunchConfigurationType configType = launchManager.getLaunchConfigurationType(LAUNCH_CONFIG_ID);

        final String configName = launchManager.generateLaunchConfigurationName(name);

        try {
            final ILaunchConfigurationWorkingCopy workingCopy = configType.newInstance(null, configName);
            for (final Decorator<ILaunchConfigurationWorkingCopy, IResource> decorator : workingCopyDecorators) {
                decorator.decorate(workingCopy, resource);
            }
            return workingCopy;
        } catch (final CoreException ex) {
            exceptionReporter.report(ex);
            return null;
        }
    }
}