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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.launcher.config.LaunchConfigurationWorkingCopyFactory;
import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Decorator;

@RunWith(JMock.class)
public class SubstepsLaunchConfigWorkingCopyFactoryTest {

    private Mockery context;

    private ILaunchManager launchManager;
    private Decorator<ILaunchConfigurationWorkingCopy, IResource> workingCopyDecorator;
    private ExceptionReporter exceptionReporter;

    private IProject project;

    private LaunchConfigurationWorkingCopyFactory workingCopyFactory;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.launchManager = context.mock(ILaunchManager.class);
        this.workingCopyDecorator = context.mock(Decorator.class);
        this.exceptionReporter = context.mock(ExceptionReporter.class);

        this.project = context.mock(IProject.class);

        this.workingCopyFactory = new SubstepsLaunchConfigWorkingCopyFactory(launchManager,
                Collections.<Decorator<ILaunchConfigurationWorkingCopy, IResource>> singleton(workingCopyDecorator),
                exceptionReporter);
    }


    @Test
    public void canCreateWorkingCopy() throws Exception {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);

        final ILaunchConfigurationType configType = context.mock(ILaunchConfigurationType.class);
        final String launchPrefixName = "name";
        final String configName = "config";

        context.checking(new Expectations() {
            {
                oneOf(launchManager).getLaunchConfigurationType("com.technophobia.substeps.junit.launchconfig");
                will(returnValue(configType));

                oneOf(launchManager).generateLaunchConfigurationName(launchPrefixName);
                will(returnValue(configName));

                oneOf(configType).newInstance(null, configName);
                will(returnValue(workingCopy));

                oneOf(workingCopyDecorator).decorate(workingCopy, project);
            }
        });

        assertThat(workingCopyFactory.create(launchPrefixName, project), is(workingCopy));
    }


    @Test
    public void creatingBadWorkingCopyGeneratesAnException() throws Exception {
        final ILaunchConfigurationType configType = context.mock(ILaunchConfigurationType.class);
        final CoreException ex = new CoreException(new Status(Status.ERROR, "plugin", "an error"));

        final String launchPrefixName = "name";
        final String configName = "config";

        context.checking(new Expectations() {
            {
                oneOf(launchManager).getLaunchConfigurationType("com.technophobia.substeps.junit.launchconfig");
                will(returnValue(configType));

                oneOf(launchManager).generateLaunchConfigurationName(launchPrefixName);
                will(returnValue(configName));

                oneOf(configType).newInstance(null, configName);
                will(throwException(ex));

                oneOf(exceptionReporter).report(ex);
            }
        });

        workingCopyFactory.create(launchPrefixName, project);
    }
}
