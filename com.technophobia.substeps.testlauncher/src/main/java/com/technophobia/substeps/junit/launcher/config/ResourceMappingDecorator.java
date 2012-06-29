package com.technophobia.substeps.junit.launcher.config;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Decorator;
import com.technophobia.substeps.junit.launcher.migration.SubstepsMigrationDelegate;

public class ResourceMappingDecorator implements Decorator<ILaunchConfigurationWorkingCopy, IResource> {

    private final ExceptionReporter exceptionReporter;


    public ResourceMappingDecorator(final ExceptionReporter exceptionReporter) {
        this.exceptionReporter = exceptionReporter;
    }


    @Override
    public void decorate(final ILaunchConfigurationWorkingCopy workingCopy, final IResource with) {
        try {
            SubstepsMigrationDelegate.mapResources(workingCopy);
        } catch (final CoreException ex) {
            exceptionReporter.report(ex);
        }
    }
}
