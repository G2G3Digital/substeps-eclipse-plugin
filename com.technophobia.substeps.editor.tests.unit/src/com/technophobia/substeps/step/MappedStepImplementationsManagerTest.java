package com.technophobia.substeps.step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class MappedStepImplementationsManagerTest {

    private Mockery context;

    private Transformer<IResource, String> resourceTransformer;
    private Transformer<String, List<StepImplementationsDescriptor>> stepImplementationLoader;

    private MappedStepImplementationsManager<String> stepImplementationManager;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.resourceTransformer = context.mock(Transformer.class, "resourceTransformer");
        this.stepImplementationLoader = context.mock(Transformer.class, "stepImplementationLoader");

        this.stepImplementationManager = new MappedStepImplementationsManager<String>(resourceTransformer,
                stepImplementationLoader);
    }


    @Test
    public void returnsStepImplementationsForResource() {
        final StepImplementationsDescriptor step1 = new StepImplementationsDescriptor("Class1");
        final StepImplementationsDescriptor step2 = new StepImplementationsDescriptor("Class2");

        final IResource resource = context.mock(IResource.class);
        context.checking(new Expectations() {
            {
                oneOf(resourceTransformer).from(resource);
                will(returnValue("a-key"));

                oneOf(stepImplementationLoader).from("a-key");
                will(returnValue(Arrays.asList(step1, step2)));
            }
        });

        stepImplementationManager.load("a-key");

        final List<StepImplementationsDescriptor> stepImplementations = stepImplementationManager
                .stepImplementationsFor(resource);
        assertThat(stepImplementations, hasItems(step1, step2));
    }


    @Test
    public void resourceWithNoStepImplementationsReturnsEmptyList() {
        final IResource resource = context.mock(IResource.class);
        context.checking(new Expectations() {
            {
                oneOf(resourceTransformer).from(resource);
                will(returnValue("a-key"));
            }
        });
        final List<StepImplementationsDescriptor> stepImplementations = stepImplementationManager
                .stepImplementationsFor(resource);
        assertTrue(stepImplementations.isEmpty());
    }
}
