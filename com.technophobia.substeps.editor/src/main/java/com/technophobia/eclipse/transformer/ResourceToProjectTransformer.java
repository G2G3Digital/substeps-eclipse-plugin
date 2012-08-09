package com.technophobia.eclipse.transformer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.technophobia.substeps.supplier.Transformer;

public class ResourceToProjectTransformer implements Transformer<IResource, IProject> {

    @Override
    public IProject from(final IResource from) {
        return from.getProject();
    }

}
