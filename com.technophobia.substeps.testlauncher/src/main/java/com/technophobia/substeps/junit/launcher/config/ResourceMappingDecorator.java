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
