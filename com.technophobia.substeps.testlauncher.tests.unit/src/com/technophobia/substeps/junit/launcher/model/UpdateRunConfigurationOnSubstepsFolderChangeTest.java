package com.technophobia.substeps.junit.launcher.model;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.core.runtime.IPath;
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

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;
import com.technophobia.substeps.event.SubstepsFolderChangedListener;
import com.technophobia.substeps.junit.launcher.config.SubstepsLaunchConfigWorkingCopyFactory;

@RunWith(JMock.class)
public class UpdateRunConfigurationOnSubstepsFolderChangeTest {

    private Mockery context;

    private ILaunchManager launchManager;

    private SubstepsFolderChangedListener folderChangedListener;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.launchManager = context.mock(ILaunchManager.class);

        this.folderChangedListener = new UpdateRunConfigurationOnSubstepsFolderChange(launchManager);
    }


    @Test
    public void reportsConfirmationNotRequiredIfThereAreNoLaunchConfigurations() throws Exception {

        prepareLaunchManagerWithLaunchConfigurations();

        assertThat(folderChangedListener.isConfirmationRequired(aPathOf("previous"), aPathOf("next")), is(false));
    }


    @Test
    public void reportsConfirmationNotRequiredIfThereAreNoApplicableLaunchConfigurations() throws Exception {

        prepareLaunchManagerWithLaunchConfigurations(aLaunchConfigurationWithPath("different"),
                aLaunchConfigurationWithPath("different2"));

        assertThat(folderChangedListener.isConfirmationRequired(aPathOf("previous"), aPathOf("next")), is(false));
    }


    @Test
    public void reportsConfirmationRequiredIfThereAreApplicableLaunchConfigurations() throws Exception {

        prepareLaunchManagerWithLaunchConfigurations(aLaunchConfigurationWithPath("different"),
                aLaunchConfigurationWithPath("previous", "previous1"),
                aLaunchConfigurationWithPath("previous", "previous2"));

        assertThat(folderChangedListener.isConfirmationRequired(aPathOf("previous"), aPathOf("next")), is(true));
    }


    @Test
    public void confirmationMessageDisplaysNumberOfAffectedRunConfigurations() throws Exception {
        prepareLaunchManagerWithLaunchConfigurations(aLaunchConfigurationWithPath("different", "different1"),
                aLaunchConfigurationWithPath("previous", "previous1"),
                aLaunchConfigurationWithPath("previous", "previous2"),
                aLaunchConfigurationWithPath("previous", "previous3"),
                aLaunchConfigurationWithPath("different", "different2"));

        assertThat(folderChangedListener.confirmationMessage(aPathOf("previous"), aPathOf("next")),
                is("3 Run configurations are affected by this change. Would you like to update them?"));
    }


    @Test
    public void onConfirmationDeletesAffectedLaunchConfigurationsAndReplacesThem() throws Exception {
        prepareLaunchManagerWithLaunchConfigurations(aLaunchConfigurationWithPath("different", "different1"),
                aLaunchConfigurationToDeleteWithPath("previous", "next", "previous1"),
                aLaunchConfigurationToDeleteWithPath("previous", "next", "previous2"));

        folderChangedListener.onConfirmation(aPathOf("previous"), aPathOf("next"));
    }


    private void prepareLaunchManagerWithLaunchConfigurations(final ILaunchConfiguration... launchConfigurations)
            throws Exception {
        final ILaunchConfigurationType launchConfigurationType = context.mock(ILaunchConfigurationType.class);

        context.checking(new Expectations() {
            {
                oneOf(launchManager)
                        .getLaunchConfigurationType(SubstepsLaunchConfigWorkingCopyFactory.LAUNCH_CONFIG_ID);
                will(returnValue(launchConfigurationType));

                oneOf(launchManager).getLaunchConfigurations(launchConfigurationType);
                will(returnValue(launchConfigurations));
            }
        });
    }


    private ILaunchConfiguration aLaunchConfigurationWithPath(final String pathString) throws Exception {
        return aLaunchConfigurationWithPath(pathString, "LaunchConfiguration with path " + pathString);
    }


    private ILaunchConfiguration aLaunchConfigurationWithPath(final String pathString,
            final String launchConfigurationName) throws Exception {
        final ILaunchConfiguration launchConfiguration = context.mock(ILaunchConfiguration.class,
                launchConfigurationName);

        context.checking(new Expectations() {
            {
                oneOf(launchConfiguration).getAttribute(SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE,
                        (String) null);
                will(returnValue(pathString));
            }
        });
        return launchConfiguration;
    }


    private ILaunchConfiguration aLaunchConfigurationToDeleteWithPath(final String pathString,
            final String newPathString, final String launchConfigurationName) throws Exception {
        final ILaunchConfiguration launchConfiguration = aLaunchConfigurationWithPath(pathString,
                launchConfigurationName);

        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class,
                "Working Copy: " + launchConfigurationName);

        context.checking(new Expectations() {
            {
                oneOf(launchConfiguration).getName();
                will(returnValue(launchConfigurationName));

                oneOf(launchConfiguration).copy(":::" + launchConfigurationName);
                will(returnValue(workingCopy));

                oneOf(launchConfiguration).delete();

                oneOf(workingCopy).setAttribute(SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE, newPathString);

                oneOf(workingCopy).rename(launchConfigurationName);
                oneOf(workingCopy).doSave();
            }
        });
        return launchConfiguration;
    }


    private IPath aPathOf(final String pathString) {
        final IPath path = context.mock(IPath.class, "Path: " + pathString);

        context.checking(new Expectations() {
            {
                allowing(path).toOSString();
                will(returnValue(pathString));
            }
        });
        return path;
    }
}
