package com.technophobia.substeps.document.partition;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.technophobia.substeps.FeatureEditorPlugin;

public class CurrentSelectionPartitionContext implements PartitionContext {

    @Override
    public IProject currentProject() {
        final IResource resource = FeatureEditorPlugin.instance().currentResourceSupplier().get();

        return resource.getProject();
    }
}
