package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class StepImplementationDescriptorJavadocLocatorTest {

    private Mockery context;

    private StepDescriptorProvider stepDescriptorProvider;
    private StepImplTypeContext stepImplTypeContext;
    private IJavaProject javaProject;
    private IProject project;

    private ProjectJavaDocLocator<StepImplTypeContext> stepDescriptorJavadocLocator;

    private Transformer<StepDescriptor, String> stepDescriptorToStringTransformer;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() throws Exception {
        this.context = new Mockery();

        this.stepDescriptorProvider = context.mock(StepDescriptorProvider.class);
        this.stepImplTypeContext = StepImplTypeContext.with(context.mock(IType.class), new StepImplementation(
                String.class, "A keyword", "Step value", String.class.getMethod("toString")));
        this.stepDescriptorToStringTransformer = context.mock(Transformer.class, "stepDescriptorToStringTransformer");
        this.javaProject = context.mock(IJavaProject.class);
        this.project = context.mock(IProject.class);

        this.stepDescriptorJavadocLocator = new StepImplementationDescriptorJavadocLocator(
                stepDescriptorToStringTransformer, stepDescriptorProvider);
    }


    @Test
    public void returnsJavaDocIfStepDescriptorLocated() {

        final String javadoc = "Some javadoc";

        final StepDescriptor descriptor1 = createDescriptor("example1", "description1");
        final StepDescriptor descriptor2 = createDescriptor("example2", "description2");
        final StepDescriptor descriptor3 = createDescriptor("example2", "description3");
        final StepDescriptor descriptor4 = createDescriptor("Step value", "description4");

        context.checking(new Expectations() {
            {
                oneOf(javaProject).getProject();
                will(returnValue(project));

                oneOf(stepDescriptorProvider).descriptorsForClassInProject(String.class.getName(), project);
                will(returnValue(Arrays.asList(descriptor1, descriptor2, descriptor3, descriptor4)));

                oneOf(stepDescriptorToStringTransformer).from(descriptor4);
                will(returnValue(javadoc));
            }
        });

        assertThat(stepDescriptorJavadocLocator.formattedJavaDocFor(stepImplTypeContext, javaProject), is(javadoc));
    }


    @Test
    public void returnsNullIfStepDescriptorNotLocated() {
        final StepDescriptor descriptor1 = createDescriptor("example1", "description1");
        final StepDescriptor descriptor2 = createDescriptor("example2", "description2");
        final StepDescriptor descriptor3 = createDescriptor("example2", "description3");
        final StepDescriptor descriptor4 = createDescriptor("example4", "description4");

        context.checking(new Expectations() {
            {
                oneOf(javaProject).getProject();
                will(returnValue(project));

                oneOf(stepDescriptorProvider).descriptorsForClassInProject(String.class.getName(), project);
                will(returnValue(Arrays.asList(descriptor1, descriptor2, descriptor3, descriptor4)));
            }
        });

        assertThat(stepDescriptorJavadocLocator.formattedJavaDocFor(stepImplTypeContext, javaProject), is(nullValue()));
    }


    private StepDescriptor createDescriptor(final String example, final String description) {
        final StepDescriptor stepDescriptor = new StepDescriptor();
        stepDescriptor.setExample(example);
        stepDescriptor.setDescription(description);
        return stepDescriptor;
    }
}
