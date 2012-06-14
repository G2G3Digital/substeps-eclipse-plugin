package com.technophobia.eclipse.launcher.config;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public interface LaunchConfigurationWorkingCopyFactory {

    ILaunchConfigurationWorkingCopy create(String name, IResource resource);
}
