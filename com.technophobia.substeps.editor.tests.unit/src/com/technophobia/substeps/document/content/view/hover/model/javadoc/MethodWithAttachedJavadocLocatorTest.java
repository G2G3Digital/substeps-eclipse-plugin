package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.javadoc.ProjectJavaDocLocator;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class MethodWithAttachedJavadocLocatorTest {

    private Mockery context;

    private Transformer<StepImplTypeContext, IMethod> methodLocator;
    private StepImplTypeContext stepImplTypeContext;
    private IJavaProject project;

    private ProjectJavaDocLocator<StepImplTypeContext> methodWithAttachedJavadocLocator;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.stepImplTypeContext = StepImplTypeContext.with(context.mock(IType.class), null);
        this.project = context.mock(IJavaProject.class);

        this.methodLocator = context.mock(Transformer.class, "methodLocator");

        this.methodWithAttachedJavadocLocator = new MethodWithAttachedJavadocLocator(methodLocator);
    }


    @Test
    public void returnsNullIfMethodCannotBeLocated() {
        context.checking(new Expectations() {
            {
                oneOf(methodLocator).from(stepImplTypeContext);
                will(returnValue(null));
            }
        });

        assertThat(methodWithAttachedJavadocLocator.formattedJavaDocFor(stepImplTypeContext, project), is(nullValue()));
    }


    @Test
    public void returnsNullIfMethodsJavadocCannotBeLocated() throws Exception {

        final IMethod method = context.mock(IMethod.class);

        context.checking(new Expectations() {
            {
                oneOf(methodLocator).from(stepImplTypeContext);
                will(returnValue(method));

                oneOf(method).getAttachedJavadoc(with(any(IProgressMonitor.class)));
                will(returnValue(null));
            }
        });

        assertThat(methodWithAttachedJavadocLocator.formattedJavaDocFor(stepImplTypeContext, project), is(nullValue()));
    }


    @Test
    public void returnsJavadocIfLocated() throws Exception {
        final String javadoc = "JavaDoc";
        final IMethod method = context.mock(IMethod.class);

        context.checking(new Expectations() {
            {
                oneOf(methodLocator).from(stepImplTypeContext);
                will(returnValue(method));

                oneOf(method).getAttachedJavadoc(with(any(IProgressMonitor.class)));
                will(returnValue(javadoc));
            }
        });

        assertThat(methodWithAttachedJavadocLocator.formattedJavaDocFor(stepImplTypeContext, project), is(javadoc));
    }
}
