package com.technophobia.substeps.junit.launcher.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;
import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.event.SubstepsFolderChangedListener;
import com.technophobia.substeps.junit.launcher.config.SubstepsLaunchConfigWorkingCopyFactory;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class UpdateRunConfigurationOnSubstepsFolderChange implements SubstepsFolderChangedListener {

    private final ILaunchManager launchManager;


    public UpdateRunConfigurationOnSubstepsFolderChange() {
        this(DebugPlugin.getDefault().getLaunchManager());
    }


    public UpdateRunConfigurationOnSubstepsFolderChange(final ILaunchManager launchManager) {
        this.launchManager = launchManager;
    }


    @Override
    public boolean isConfirmationRequired(final IPath previousPath, final IPath newPath) {
        return !locatedAffectedConfigurations(previousPath).isEmpty();
    }


    @Override
    public String confirmationMessage(final IPath previousPath, final IPath newPath) {
        return MessageFormat.format(
                SubstepsFeatureMessages.SubstepsFeatureLaunchShortcut_configs_affected_update_substeps_folder,
                locatedAffectedConfigurations(previousPath).size());
    }


    @Override
    public void onConfirmation(final IPath previousPath, final IPath newPath) {
        final Collection<ILaunchConfiguration> affectedConfigurations = locatedAffectedConfigurations(previousPath);

        for (final ILaunchConfiguration launchConfiguration : affectedConfigurations) {
            final String name = launchConfiguration.getName();
            try {
                final ILaunchConfigurationWorkingCopy copy = launchConfiguration.copy(":::" + name);
                copy.setAttribute(SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE, newPath.toOSString());

                launchConfiguration.delete();
                copy.rename(name);

                copy.doSave();
            } catch (final CoreException ex) {
                FeatureRunnerPlugin.log(ex);
            }
        }
    }


    private Collection<ILaunchConfiguration> locatedAffectedConfigurations(final IPath previousPath) {
        final ILaunchConfigurationType configurationType = launchManager
                .getLaunchConfigurationType(SubstepsLaunchConfigWorkingCopyFactory.LAUNCH_CONFIG_ID);

        final String previousPathString = previousPath.toOSString();

        if (previousPathString != null) {
            final ILaunchConfiguration[] launchConfigurations = launchConfigurationsForType(configurationType);
            final Collection<ILaunchConfiguration> affectedConfigurations = new ArrayList<ILaunchConfiguration>();

            for (final ILaunchConfiguration launchConfiguration : launchConfigurations) {
                final String launchSubstepsFile = substepsFileFor(launchConfiguration);

                if (previousPath.toOSString().equals(launchSubstepsFile)) {
                    affectedConfigurations.add(launchConfiguration);
                }
            }

            return Collections.unmodifiableCollection(affectedConfigurations);
        }

        return Collections.emptyList();
    }


    private ILaunchConfiguration[] launchConfigurationsForType(final ILaunchConfigurationType configurationType) {
        try {
            return launchManager.getLaunchConfigurations(configurationType);
        } catch (final CoreException e) {
            FeatureRunnerPlugin.log(IStatus.WARNING, "Could not get launch configurations for type "
                    + configurationType.getIdentifier());
            return new ILaunchConfiguration[0];
        }
    }


    private String substepsFileFor(final ILaunchConfiguration launchConfiguration) {
        try {
            return launchConfiguration.getAttribute(SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE,
                    (String) null);
        } catch (final CoreException e) {
            FeatureRunnerPlugin.log(IStatus.WARNING, "Could not determine substeps file for launch configuration "
                    + launchConfiguration.getName());
            return null;
        }
    }
}
