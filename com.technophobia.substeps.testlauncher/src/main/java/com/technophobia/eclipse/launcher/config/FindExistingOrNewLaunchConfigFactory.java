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
