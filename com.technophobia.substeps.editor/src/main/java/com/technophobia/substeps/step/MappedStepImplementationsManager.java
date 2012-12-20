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
package com.technophobia.substeps.step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;

import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.supplier.Transformer;

public class MappedStepImplementationsManager<T> implements StepImplementationManager {

    private final Transformer<IResource, T> resourceTransformer;
    private final Transformer<T, List<StepImplementationsDescriptor>> stepImplementationLoader;
    private final Map<T, List<StepImplementationsDescriptor>> stepImplementationMap;


    public MappedStepImplementationsManager(final Transformer<IResource, T> resourceTransformer,
            final Transformer<T, List<StepImplementationsDescriptor>> stepImplementationLoader) {
        this.resourceTransformer = resourceTransformer;
        this.stepImplementationLoader = stepImplementationLoader;
        this.stepImplementationMap = new HashMap<T, List<StepImplementationsDescriptor>>();
    }


    public void load(final T t) {
        if (!stepImplementationMap.containsKey(t)) {
            stepImplementationMap.put(t, new ArrayList<StepImplementationsDescriptor>());
        }

        stepImplementationMap.get(t).addAll(stepImplementationLoader.from(t));
    }


    @Override
    public List<StepImplementationsDescriptor> stepImplementationsFor(final IResource resource) {
        final T key = resourceTransformer.from(resource);
        return stepImplementationMap.containsKey(key) ? stepImplementationMap.get(key) : Collections
                .<StepImplementationsDescriptor> emptyList();
    }
}
