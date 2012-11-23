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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Locator;

@RunWith(JMock.class)
public class FindExistingOrNewLaunchConfigFactoryTest {

    private Mockery context;

    private Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> existingConfigLocator;
    private ExceptionReporter exceptionReporter;

    private LaunchConfigurationFactory configFactory;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.existingConfigLocator = context.mock(Locator.class);
        this.exceptionReporter = context.mock(ExceptionReporter.class);

        this.configFactory = new FindExistingOrNewLaunchConfigFactory(existingConfigLocator, exceptionReporter);
    }


    @Test
    public void createsNewConfigIfNoExistingConfigsExist() throws Exception {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);
        final ILaunchConfiguration launchConfig = context.mock(ILaunchConfiguration.class);

        context.checking(new Expectations() {
            {
                oneOf(existingConfigLocator).one(workingCopy);
                will(returnValue(null));

                oneOf(workingCopy).doSave();
                will(returnValue(launchConfig));
            }
        });

        assertThat(configFactory.create(workingCopy), is(launchConfig));
    }


    @Test
    public void usesExistingConfigIfFound() throws Exception {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);
        final ILaunchConfiguration launchConfig = context.mock(ILaunchConfiguration.class);

        context.checking(new Expectations() {
            {
                oneOf(existingConfigLocator).one(workingCopy);
                will(returnValue(launchConfig));
            }
        });

        assertThat(configFactory.create(workingCopy), is(launchConfig));
    }

}
