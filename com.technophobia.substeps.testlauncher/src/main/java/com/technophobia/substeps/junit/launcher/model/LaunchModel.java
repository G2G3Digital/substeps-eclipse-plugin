package com.technophobia.substeps.junit.launcher.model;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public interface LaunchModel {

    void saveTo(final ILaunchConfigurationWorkingCopy config);

}
