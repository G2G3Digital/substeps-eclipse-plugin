/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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

    public static final String LAUNCH_CONFIG_ID = "com.technophobia.substeps.junit.launchconfig";

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
