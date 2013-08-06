package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
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
import com.technophobia.substeps.model.StepImplementation;

@RunWith(JMock.class)
public class JavaDocForStepImplementationLocatorTest {

    private Mockery context;

    private ProjectJavaDocLocator<StepImplTypeContext> sourceJavaDocLocator;
    private ProjectJavaDocLocator<StepImplTypeContext> binaryJavaDocLocator;

    private StepImplementation stepImplementation;

    private ProjectJavaDocLocator<StepImplementation> stepImplementationJavaDocLocator;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() throws Exception {
        this.context = new Mockery();

        this.sourceJavaDocLocator = context.mock(ProjectJavaDocLocator.class, "sourceJavaDocLocator");
        this.binaryJavaDocLocator = context.mock(ProjectJavaDocLocator.class, "binaryJavaDocLocator");

        this.stepImplementation = new StepImplementation(String.class, "A keyword", "A value",
                String.class.getMethod("toString"));

        this.stepImplementationJavaDocLocator = new JavaDocForStepImplementationLocator(sourceJavaDocLocator,
                binaryJavaDocLocator);
    }


    @Test
    public void returnsSourceJavaDocIfTypeIsSource() throws Exception {

        final String javadoc = "Source javadoc";

        final IType type = createType(false);
        final IJavaProject project = createProjectWith(type);

        context.checking(new Expectations() {
            {
                oneOf(sourceJavaDocLocator).formattedJavaDocFor(StepImplTypeContext.with(type, stepImplementation),
                        project);
                will(returnValue(javadoc));
            }
        });

        assertThat(stepImplementationJavaDocLocator.formattedJavaDocFor(stepImplementation, project), is(javadoc));
    }


    @Test
    public void returnsNoJavaDocMessageIfTypeIsSourceButHasNoJavadoc() throws Exception {
        final IType type = createType(false);
        final IJavaProject project = createProjectWith(type);

        context.checking(new Expectations() {
            {
                oneOf(sourceJavaDocLocator).formattedJavaDocFor(StepImplTypeContext.with(type, stepImplementation),
                        project);
                will(returnValue(null));
            }
        });

        assertThat(stepImplementationJavaDocLocator.formattedJavaDocFor(stepImplementation, project),
                startsWith("Note: Step Implementation has no attached Javadoc."));
    }


    @Test
    public void returnsBinaryJavaDocIfTypeIsBinary() throws Exception {
        final String javadoc = "Binary javadoc";

        final IType type = createType(true);
        final IJavaProject project = createProjectWith(type);

        context.checking(new Expectations() {
            {
                oneOf(binaryJavaDocLocator).formattedJavaDocFor(StepImplTypeContext.with(type, stepImplementation),
                        project);
                will(returnValue(javadoc));
            }
        });

        assertThat(stepImplementationJavaDocLocator.formattedJavaDocFor(stepImplementation, project), is(javadoc));
    }


    @Test
    public void returnsNoJavaDocMessageIfTypeIsBinaryButHasNoJavadoc() throws Exception {
        final IType type = createType(true);
        final IJavaProject project = createProjectWith(type);

        context.checking(new Expectations() {
            {
                oneOf(binaryJavaDocLocator).formattedJavaDocFor(StepImplTypeContext.with(type, stepImplementation),
                        project);
                will(returnValue(null));
            }
        });

        assertThat(stepImplementationJavaDocLocator.formattedJavaDocFor(stepImplementation, project),
                startsWith("Note: Step Implementation has no attached Javadoc."));
    }


    @Test
    public void returnsNullIfTypeCannotBeLocated() throws Exception {
        final IType type = null;
        final IJavaProject project = createProjectWith(type);

        assertThat(stepImplementationJavaDocLocator.formattedJavaDocFor(stepImplementation, project), is(nullValue()));
    }


    private IType createType(final boolean binary) {
        final IType type = context.mock(IType.class);
        context.checking(new Expectations() {
            {
                oneOf(type).isBinary();
                will(returnValue(binary));
            }
        });
        return type;
    }


    private IJavaProject createProjectWith(final IType type) throws Exception {
        final IJavaProject project = context.mock(IJavaProject.class);
        context.checking(new Expectations() {
            {
                oneOf(project).findType(String.class.getName());
                will(returnValue(type));
            }
        });
        return project;
    }
}
