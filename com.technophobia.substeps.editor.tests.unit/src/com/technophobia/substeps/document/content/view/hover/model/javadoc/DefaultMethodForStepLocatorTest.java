package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.model.StepImplementation;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class DefaultMethodForStepLocatorTest {

    private static final String ANNOTATION_NAME = "An annotation";
    private static final String STEP_IMPLEMENTATION_VALUE = "A Value";

    private Mockery context;

    private IType type;
    private StepImplementation stepImplementation;
    private StepImplTypeContext stepImplTypeContext;

    private Transformer<StepImplTypeContext, IMethod> methodLocator;


    @Before
    public void initialise() throws Exception {
        this.context = new Mockery();

        this.type = context.mock(IType.class);
        this.stepImplementation = new StepImplementation(String.class, "A Keyword", STEP_IMPLEMENTATION_VALUE,
                String.class.getMethod("toString"));
        this.stepImplTypeContext = StepImplTypeContext.with(type, stepImplementation);

        this.methodLocator = new DefaultMethodForStepLocator(ANNOTATION_NAME);
    }


    @Test
    public void returnsNullIfMethodCannotBeFound() throws Exception {
        final IMethod method1 = aMethod("method1");
        final IMethod method2 = aMethod("method2", anAnnotationNamed("other annotation", "method2"));
        final IMethod method3 = aMethod("method3", anAnnotationNamed("otherAnnotation", "method3"),
                aStepAnnotation("some other step", "method3"));

        context.checking(new Expectations() {
            {
                oneOf(type).getMethods();
                will(returnValue(new IMethod[] { method1, method2, method3 }));
            }
        });

        assertThat(methodLocator.from(stepImplTypeContext), is(nullValue()));
    }


    @Test
    public void locatesCorrectMethod() throws Exception {
        final IMethod method1 = aMethod("method1");
        final IMethod method2 = aMethod("method2", anAnnotationNamed("other annotation", "method2"));
        final IMethod method3 = aMethod("method3", anAnnotationNamed("otherAnnotation", "method3"),
                aStepAnnotation("some other step", "method3"));
        final IMethod targetMethod = aMethod("targetMethod", aStepAnnotation(STEP_IMPLEMENTATION_VALUE, "targetMethod"));

        context.checking(new Expectations() {
            {
                oneOf(type).getMethods();
                will(returnValue(new IMethod[] { method1, method2, method3, targetMethod }));
            }
        });

        assertThat(methodLocator.from(stepImplTypeContext), is(targetMethod));
    }


    private IMethod aMethod(final String methodName, final IAnnotation... annotations) throws Exception {
        final IMethod method = context.mock(IMethod.class, methodName);

        context.checking(new Expectations() {
            {
                oneOf(method).getAnnotations();
                will(returnValue(annotations));
            }
        });

        return method;
    }


    private IAnnotation anAnnotationNamed(final String annotationName, final String methodName) {
        final IAnnotation annotation = context.mock(IAnnotation.class, methodName + "." + annotationName);

        context.checking(new Expectations() {
            {
                oneOf(annotation).getElementName();
                will(returnValue(annotationName));
            }
        });

        return annotation;
    }


    private IAnnotation aStepAnnotation(final String stepValue, final String methodName) throws Exception {
        final IAnnotation annotation = context.mock(IAnnotation.class, methodName + "." + ANNOTATION_NAME);

        final IMemberValuePair mvp = context.mock(IMemberValuePair.class, methodName + "." + ANNOTATION_NAME
                + "(value : " + stepValue + ")");

        context.checking(new Expectations() {
            {
                oneOf(annotation).getElementName();
                will(returnValue(ANNOTATION_NAME));

                oneOf(mvp).getMemberName();
                will(returnValue("value"));
                oneOf(mvp).getValue();
                will(returnValue(stepValue));

                oneOf(annotation).getMemberValuePairs();
                will(returnValue(new IMemberValuePair[] { mvp }));
            }
        });

        return annotation;
    }
}
