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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Locator;
import com.technophobia.substeps.supplier.Transformer;

public class WorkingCopyLaunchConfigLocator implements Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> {

    private final String[] matchingAttributeNames;
    private final ILaunchManager launchManager;
    private final ExceptionReporter exceptionReporter;
    private final Transformer<Collection<ILaunchConfiguration>, ILaunchConfiguration> multiConfigResolver;


    public WorkingCopyLaunchConfigLocator(final String[] matchingAttributeNames, final ILaunchManager launchManager,
            final Transformer<Collection<ILaunchConfiguration>, ILaunchConfiguration> multiConfigResolver,
            final ExceptionReporter exceptionReporter) {
        this.matchingAttributeNames = matchingAttributeNames;
        this.launchManager = launchManager;
        this.multiConfigResolver = multiConfigResolver;
        this.exceptionReporter = exceptionReporter;
    }


    @Override
    public Collection<ILaunchConfiguration> all(final ILaunchConfigurationWorkingCopy workingCopy) {
        final ILaunchConfiguration[] configurations = launchConfigurations(workingCopy);
        final Collection<ILaunchConfiguration> matchingConfigurations = new ArrayList<ILaunchConfiguration>();
        for (final ILaunchConfiguration configuration : configurations) {
            if (attributesMatch(configuration, workingCopy)) {
                matchingConfigurations.add(configuration);
            }

        }
        return matchingConfigurations;
    }


    private ILaunchConfiguration[] launchConfigurations(final ILaunchConfigurationWorkingCopy workingCopy) {
        try {
            return launchManager.getLaunchConfigurations(workingCopy.getType());
        } catch (final CoreException ex) {
            exceptionReporter.report(ex);
            return new ILaunchConfiguration[0];
        }
    }


    @Override
    public ILaunchConfiguration one(final ILaunchConfigurationWorkingCopy workingCopy) {
        final Collection<ILaunchConfiguration> all = all(workingCopy);
        if (all.isEmpty()) {
            return null;
        } else if (all.size() == 1) {
            return all.iterator().next();
        }
        return multiConfigResolver.from(all);
    }


    private boolean attributesMatch(final ILaunchConfiguration configuration,
            final ILaunchConfigurationWorkingCopy workingCopy) {
        try {
            for (final String attributeName : matchingAttributeNames) {
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
