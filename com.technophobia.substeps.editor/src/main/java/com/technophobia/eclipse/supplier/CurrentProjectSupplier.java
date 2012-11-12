package com.technophobia.eclipse.supplier;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.technophobia.eclipse.transformer.ResourceToProjectTransformer;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

public class CurrentProjectSupplier implements Supplier<IProject> {

    private final Transformer<IResource, IProject> resourceToProjectTransformer;
    private final Supplier<IResource> resourceSupplier;


    public CurrentProjectSupplier() {
        this(FeatureEditorPlugin.instance().currentResourceSupplier(), new ResourceToProjectTransformer());
    }


    public CurrentProjectSupplier(final Supplier<IResource> resourceSupplier,
            final Transformer<IResource, IProject> resourceToProjectTransformer) {
        this.resourceSupplier = resourceSupplier;
        this.resourceToProjectTransformer = resourceToProjectTransformer;
    }


    @Override
    public IProject get() {
        final IResource resource = resourceSupplier.get();
        if (resource != null) {
            return resourceToProjectTransformer.from(resource);
        }
        return null;
    }

}
