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
