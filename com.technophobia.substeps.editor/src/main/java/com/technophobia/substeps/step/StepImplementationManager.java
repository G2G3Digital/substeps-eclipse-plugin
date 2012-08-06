package com.technophobia.substeps.step;

import java.util.List;

import org.eclipse.core.resources.IResource;

import com.technophobia.substeps.glossary.StepImplementationsDescriptor;

public interface StepImplementationManager {

    List<StepImplementationsDescriptor> stepImplementationsFor(IResource resource);
}
