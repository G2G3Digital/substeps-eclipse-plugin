package com.technophobia.eclipse.launcher.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
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
import com.technophobia.eclipse.transformer.Locator;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class WorkingCopyLaunchConfigLocatorTest {

    private Mockery context;

    private Locator<ILaunchConfiguration, ILaunchConfigurationWorkingCopy> locator;

    private ILaunchManager launchManager;

    private Transformer<Collection<ILaunchConfiguration>, ILaunchConfiguration> multiConfigResolver;

    private ExceptionReporter exceptionReporter;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.launchManager = context.mock(ILaunchManager.class);
        this.multiConfigResolver = context.mock(Transformer.class);
        this.exceptionReporter = context.mock(ExceptionReporter.class);

        this.locator = new WorkingCopyLaunchConfigLocator(new String[] { "attribute-name" }, launchManager,
                multiConfigResolver, exceptionReporter);
    }


    @Test
    public void returnsAllMatchingConfigurations() throws Exception {

        final ILaunchConfiguration item1 = createLaunchConfig("config-1", "valid-attribute");
        final ILaunchConfiguration item2 = createLaunchConfig("config-2", "invalid-attribute");
        final ILaunchConfiguration item3 = createLaunchConfig("config-3", "valid-attribute");

        final ILaunchConfigurationWorkingCopy workingCopy = prepareLaunchConfigurations(item1, item2, item3);

        final Collection<ILaunchConfiguration> matchingConfigurations = locator.all(workingCopy);
        assertThat(matchingConfigurations.size(), is(2));
        assertThat(matchingConfigurations, hasItems(item1, item3));
    }


    @Test
    public void returnsCorrectSingleLaunchConfig() throws Exception {
        final ILaunchConfiguration item = createLaunchConfig("config", "valid-attribute");

        final ILaunchConfigurationWorkingCopy workingCopy = prepareLaunchConfigurations(item);

        final ILaunchConfiguration config = locator.one(workingCopy);
        assertThat(config, is(notNullValue()));
        assertThat(config, is(item));
    }


    @Test
    public void returnsNullIfLaunchConfigNotFound() throws Exception {
        final ILaunchConfiguration item1 = createLaunchConfig("config-1", "invalid-attribute");
        final ILaunchConfiguration item2 = createLaunchConfig("config-2", "invalid-attribute");

        final ILaunchConfigurationWorkingCopy workingCopy = prepareLaunchConfigurations(item1, item2);

        final ILaunchConfiguration config = locator.one(workingCopy);
        assertThat(config, is(nullValue()));
    }


    @SuppressWarnings("unchecked")
    @Test
    public void defersToConfigResolverIfMultipleMatchesFound() throws Exception {
        final ILaunchConfiguration item1 = createLaunchConfig("config-1", "valid-attribute");
        final ILaunchConfiguration item2 = createLaunchConfig("config-2", "valid-attribute");

        final ILaunchConfigurationWorkingCopy workingCopy = prepareLaunchConfigurations(item1, item2);

        context.checking(new Expectations() {
            {
                oneOf(multiConfigResolver).from(with(any(Collection.class)));
                will(returnValue(item2));
            }
        });

        final ILaunchConfiguration config = locator.one(workingCopy);
        assertThat(config, is(notNullValue()));
        assertThat(config, is(item2));
    }


    private ILaunchConfiguration createLaunchConfig(final String name, final String attributeValue) throws Exception {
        final ILaunchConfiguration launchConfiguration = context.mock(ILaunchConfiguration.class, name);

        context.checking(new Expectations() {
            {
                oneOf(launchConfiguration).hasAttribute("attribute-name");
                will(returnValue(true));

                oneOf(launchConfiguration).getAttribute("attribute-name", "not-defined");
                will(returnValue(attributeValue));
            }
        });
        return launchConfiguration;
    }


    private ILaunchConfigurationWorkingCopy prepareLaunchConfigurations(final ILaunchConfiguration... items)
            throws CoreException {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);
        final ILaunchConfigurationType configType = context.mock(ILaunchConfigurationType.class);
        context.checking(new Expectations() {
            {
                oneOf(workingCopy).getType();
                will(returnValue(configType));

                oneOf(launchManager).getLaunchConfigurations(configType);
                will(returnValue(items));

                exactly(items.length).of(workingCopy).getAttribute("attribute-name", "");
                will(returnValue("valid-attribute"));
            }
        });
        return workingCopy;
    }
}
