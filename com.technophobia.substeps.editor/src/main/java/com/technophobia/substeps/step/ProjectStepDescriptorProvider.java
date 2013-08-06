package com.technophobia.substeps.step;

import java.util.List;

import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.glossary.StepDescriptor;

public interface ProjectStepDescriptorProvider {

    List<StepDescriptor> stepDescriptorsFor(IProject project, String className);
}
