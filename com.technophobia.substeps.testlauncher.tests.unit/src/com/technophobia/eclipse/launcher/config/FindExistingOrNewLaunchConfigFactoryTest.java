package com.technophobia.eclipse.launcher.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;

@RunWith(JMock.class)
public class FindExistingOrNewLaunchConfigFactoryTest {

    private Mockery context;

    private ILaunchManager launchManager;
    private ExceptionReporter exceptionReporter;

    private LaunchConfigurationFactory configFactory;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.launchManager = context.mock(ILaunchManager.class);
        this.exceptionReporter = context.mock(ExceptionReporter.class);

        this.configFactory = new FindExistingOrNewLaunchConfigFactory(new String[] { "attribute-name" }, launchManager,
                exceptionReporter);
    }


    @Test
    public void createsNewConfigIfNoExistingConfigsExist() throws Exception {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);
        final ILaunchConfiguration launchConfig = context.mock(ILaunchConfiguration.class);
        final ILaunchConfigurationType configType = context.mock(ILaunchConfigurationType.class);

        context.checking(new Expectations() {
            {
                oneOf(workingCopy).getType();
                will(returnValue(configType));

                oneOf(launchManager).getLaunchConfigurations(configType);
                will(returnValue(new ILaunchConfiguration[0]));

                oneOf(workingCopy).doSave();
                will(returnValue(launchConfig));
            }
        });

        assertThat(configFactory.create(workingCopy), is(launchConfig));
    }


    @Test
    public void createsNewConfigIfNoMatchingConfigExists() throws Exception {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);
        final ILaunchConfiguration launchConfig = context.mock(ILaunchConfiguration.class);
        final ILaunchConfigurationType configType = context.mock(ILaunchConfigurationType.class);

        final ILaunchConfiguration[] existingConfigs = new ILaunchConfiguration[] { //
        createConfig("attribute-1"), //
                createConfig("attribute-2"), //
                createConfig("attribute-3") //
        };

        context.checking(new Expectations() {
            {
                oneOf(workingCopy).getType();
                will(returnValue(configType));

                exactly(3).of(workingCopy).getAttribute("attribute-name", "");
                will(returnValue("attribute-0"));

                oneOf(launchManager).getLaunchConfigurations(configType);
                will(returnValue(existingConfigs));

                oneOf(workingCopy).doSave();
                will(returnValue(launchConfig));
            }
        });

        assertThat(configFactory.create(workingCopy), is(launchConfig));
    }


    @Test
    public void usesExistingConfigIfFound() throws Exception {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);
        final ILaunchConfiguration launchConfig = createConfig("attribute-3");
        final ILaunchConfigurationType configType = context.mock(ILaunchConfigurationType.class);

        final ILaunchConfiguration[] existingConfigs = new ILaunchConfiguration[] { //
        createConfig("attribute-1"), //
                createConfig("attribute-2"), //
                launchConfig //
        };

        context.checking(new Expectations() {
            {
                oneOf(workingCopy).getType();
                will(returnValue(configType));

                exactly(3).of(workingCopy).getAttribute("attribute-name", "");
                will(returnValue("attribute-3"));

                oneOf(launchManager).getLaunchConfigurations(configType);
                will(returnValue(existingConfigs));

            }
        });

        assertThat(configFactory.create(workingCopy), is(launchConfig));
    }


    private ILaunchConfiguration createConfig(final String featureFile) throws Exception {
        final ILaunchConfiguration launchConfig = context.mock(ILaunchConfiguration.class, "config-for-" + featureFile);

        context.checking(new Expectations() {
            {
                oneOf(launchConfig).hasAttribute("attribute-name");
                will(returnValue(true));

                oneOf(launchConfig).getAttribute("attribute-name", "not-defined");
                will(returnValue(featureFile));
            }
        });
        return launchConfig;
    }
}
