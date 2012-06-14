package com.technophobia.eclipse.launcher.config;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public interface LaunchConfigurationFactory {

	ILaunchConfiguration create(ILaunchConfigurationWorkingCopy workingCopy);
}
