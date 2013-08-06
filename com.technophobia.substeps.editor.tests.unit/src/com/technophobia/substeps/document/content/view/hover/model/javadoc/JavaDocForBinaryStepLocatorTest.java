package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;

@RunWith(JMock.class)
public class JavaDocForBinaryStepLocatorTest {

    private Mockery context;

    private ProjectJavaDocLocator<StepImplTypeContext> subProvider1;
    private ProjectJavaDocLocator<StepImplTypeContext> subProvider2;
    private ProjectJavaDocLocator<StepImplTypeContext> subProvider3;

    private StepImplTypeContext stepImplTypeContext;
    private IJavaProject project;

    private ProjectJavaDocLocator<StepImplTypeContext> binaryProvider;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.stepImplTypeContext = StepImplTypeContext.with(context.mock(IType.class), null);
        this.project = context.mock(IJavaProject.class);

        this.subProvider1 = context.mock(ProjectJavaDocLocator.class, "subProvider1");
        this.subProvider2 = context.mock(ProjectJavaDocLocator.class, "subProvider2");
        this.subProvider3 = context.mock(ProjectJavaDocLocator.class, "subProvider3");

        this.binaryProvider = new JavaDocForBinaryStepLocator(subProvider1, subProvider2, subProvider3);
    }


    @Test
    public void returnsJavadocFromFirstSubProviderIfValid() {

        final String javadoc = "JavaDoc";

        context.checking(new Expectations() {
            {
                oneOf(subProvider1).formattedJavaDocFor(stepImplTypeContext, project);
                will(returnValue(javadoc));
            }
        });

        assertThat(binaryProvider.formattedJavaDocFor(stepImplTypeContext, project), is(javadoc));
    }


    @Test
    public void returnsJavadocFromLastSubProviderIfValid() {
        final String javadoc = "JavaDoc";

        context.checking(new Expectations() {
            {
                oneOf(subProvider1).formattedJavaDocFor(stepImplTypeContext, project);
                will(returnValue(null));

                oneOf(subProvider2).formattedJavaDocFor(stepImplTypeContext, project);
                will(returnValue(null));

                oneOf(subProvider3).formattedJavaDocFor(stepImplTypeContext, project);
                will(returnValue(javadoc));
            }
        });

        assertThat(binaryProvider.formattedJavaDocFor(stepImplTypeContext, project), is(javadoc));
    }


    @Test
    public void returnsNullIfNoSubProviderIsValid() {
        context.checking(new Expectations() {
            {
                oneOf(subProvider1).formattedJavaDocFor(stepImplTypeContext, project);
                will(returnValue(null));

                oneOf(subProvider2).formattedJavaDocFor(stepImplTypeContext, project);
                will(returnValue(null));

                oneOf(subProvider3).formattedJavaDocFor(stepImplTypeContext, project);
                will(returnValue(null));
            }
        });

        assertThat(binaryProvider.formattedJavaDocFor(stepImplTypeContext, project), is(nullValue()));
    }
}
