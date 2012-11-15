package com.technophobia.substeps.junit.launcher.config;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.technophobia.eclipse.transformer.Decorator;
import com.technophobia.substeps.junit.launcher.model.LaunchModelFactory;

public class SubstepsLaunchConfigWorkingCopyDecorator implements Decorator<ILaunchConfigurationWorkingCopy, IResource> {

    public static final String FEATURE_TEST = "com.technophobia.substeps.runner.runtime.DefinableFeatureTest";

    private final LaunchModelFactory launchModelFactory;


    public SubstepsLaunchConfigWorkingCopyDecorator(final LaunchModelFactory launchModelFactory) {
        this.launchModelFactory = launchModelFactory;
    }


    @Override
    public void decorate(final ILaunchConfigurationWorkingCopy workingCopy, final IResource resource) {

        launchModelFactory.createFrom(resource).saveTo(workingCopy);
    }
}
