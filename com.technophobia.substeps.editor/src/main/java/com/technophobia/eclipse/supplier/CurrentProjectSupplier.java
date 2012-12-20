/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
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
