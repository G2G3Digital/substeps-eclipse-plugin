package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import java.util.List;

import org.eclipse.core.resources.IProject;

import com.technophobia.substeps.glossary.StepDescriptor;

public interface StepDescriptorProvider {
    List<StepDescriptor> descriptorsForClassInProject(String className, IProject project);
}
