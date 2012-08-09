package com.technophobia.substeps.step;

import java.util.Collection;

import org.eclipse.core.resources.IProject;

public interface ProjectStepImplementationProvider {

    Collection<String> stepImplementationClasses(IProject project);
}
