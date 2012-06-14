package com.technophobia.substeps.junit.launcher.config;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.internal.junit.launcher.JUnitMigrationDelegate;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Decorator;

@SuppressWarnings("restriction")
public class JunitResourceMappingDecorator implements Decorator<ILaunchConfigurationWorkingCopy, IResource> {

    private final ExceptionReporter exceptionReporter;


    public JunitResourceMappingDecorator(final ExceptionReporter exceptionReporter) {
        this.exceptionReporter = exceptionReporter;
    }


    @Override
    public void decorate(final ILaunchConfigurationWorkingCopy workingCopy, final IResource with) {
        try {
            JUnitMigrationDelegate.mapResources(workingCopy);
        } catch (final CoreException ex) {
            exceptionReporter.report(ex);
        }
    }
}
