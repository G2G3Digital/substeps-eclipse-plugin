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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class CurrentProjectSupplierTest {

    private Mockery context;

    private Supplier<IResource> resourceSupplier;
    private Transformer<IResource, IProject> resourceToProjectTransformer;

    private Supplier<IProject> projectSupplier;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.resourceSupplier = context.mock(Supplier.class);
        this.resourceToProjectTransformer = context.mock(Transformer.class);

        this.projectSupplier = new CurrentProjectSupplier(resourceSupplier, resourceToProjectTransformer);
    }


    @Test
    public void canGetCurrentProjectForValidResource() {

        final IResource resource = context.mock(IResource.class);
        final IProject project = context.mock(IProject.class);

        context.checking(new Expectations() {
            {
                oneOf(resourceSupplier).get();
                will(returnValue(resource));

                oneOf(resourceToProjectTransformer).from(resource);
                will(returnValue(project));
            }
        });

        assertThat(projectSupplier.get(), is(project));
    }


    @Test
    public void nullResourceReturnsNullProject() {
        context.checking(new Expectations() {
            {
                oneOf(resourceSupplier).get();
                will(returnValue(null));
            }
        });

        assertThat(projectSupplier.get(), is(nullValue()));
    }
}
