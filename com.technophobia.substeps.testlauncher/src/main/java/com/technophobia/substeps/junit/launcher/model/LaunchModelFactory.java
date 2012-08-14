package com.technophobia.substeps.junit.launcher.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfiguration;

public interface LaunchModelFactory {

    LaunchModel createFrom(IResource resource);


    LaunchModel createFrom(ILaunchConfiguration config);
}
