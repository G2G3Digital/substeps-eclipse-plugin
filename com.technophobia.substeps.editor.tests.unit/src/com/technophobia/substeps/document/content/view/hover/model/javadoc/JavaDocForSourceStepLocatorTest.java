package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

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
public class JavaDocForSourceStepLocatorTest {

    private Mockery context;

    private Transformer<StepImplTypeContext, IMethod> methodForStepLocator;
    private Transformer<IMethod, String> javadocForSourcMethodTransformer;

    private StepImplTypeContext stepImplTypeContext;

    private ProjectJavaDocLocator<StepImplTypeContext> javadocForSourceStepLocator;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.methodForStepLocator = context.mock(Transformer.class, "methodForStepLocator");
        this.javadocForSourcMethodTransformer = context.mock(Transformer.class, "javaDocForSourcMethodTransformer");
        this.stepImplTypeContext = StepImplTypeContext.with(context.mock(IType.class), null);

        this.javadocForSourceStepLocator = new JavaDocForSourceStepLocator(methodForStepLocator,
                javadocForSourcMethodTransformer);
    }


    @Test
    public void returnsJavaDocIfMethodIsLocated() {
        final String javadoc = "Javadoc";
        final IMethod method = context.mock(IMethod.class);

        context.checking(new Expectations() {
            {
                oneOf(methodForStepLocator).from(stepImplTypeContext);
                will(returnValue(method));

                oneOf(javadocForSourcMethodTransformer).from(method);
                will(returnValue(javadoc));
            }
        });

        assertThat(
                javadocForSourceStepLocator.formattedJavaDocFor(stepImplTypeContext, context.mock(IJavaProject.class)),
                is(javadoc));
    }


    @Test
    public void returnsNullIfMethodIsNotLocated() {

        context.checking(new Expectations() {
            {
                oneOf(methodForStepLocator).from(stepImplTypeContext);
                will(returnValue(null));
            }
        });

        assertThat(
                javadocForSourceStepLocator.formattedJavaDocFor(stepImplTypeContext, context.mock(IJavaProject.class)),
                is(nullValue()));
    }
}
