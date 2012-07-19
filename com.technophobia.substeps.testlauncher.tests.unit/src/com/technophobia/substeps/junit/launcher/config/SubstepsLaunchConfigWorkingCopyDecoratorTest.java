package com.technophobia.substeps.junit.launcher.config;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.launcher.exception.ExceptionReporter;
import com.technophobia.eclipse.transformer.Decorator;
import com.technophobia.eclipse.transformer.Transformer;

@SuppressWarnings("restriction")
@RunWith(JMock.class)
public class SubstepsLaunchConfigWorkingCopyDecoratorTest {

    private Mockery context;

    private IResource resource;
    private IProject project;

    private Transformer<IProject, IJavaProject> transformer;
    private ExceptionReporter exceptionReporter;

    private Decorator<ILaunchConfigurationWorkingCopy, IResource> decorator;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.resource = context.mock(IResource.class);
        this.project = context.mock(IProject.class);

        this.transformer = context.mock(Transformer.class);
        this.exceptionReporter = context.mock(ExceptionReporter.class);

        this.decorator = new SubstepsLaunchConfigWorkingCopyDecorator(transformer, exceptionReporter);
    }


    @Test
    public void canDecorate() throws Exception {

        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);
        final String pathString = "/file/path";
        final IPath resourcePath = context.mock(IPath.class, "resource");

        final String outputFolder = "/output";
        final IPath outputFolderPath = context.mock(IPath.class, "outputFolder");
        final IPath strippedOutputFolderPath = context.mock(IPath.class, "strippedOutputFolder");

        final IJavaProject javaProject = context.mock(IJavaProject.class);

        context.checking(new Expectations() {
            {
                oneOf(resource).getProject();
                will(returnValue(project));

                oneOf(resource).getRawLocation();
                will(returnValue(resourcePath));

                oneOf(resourcePath).toOSString();
                will(returnValue(pathString));

                oneOf(project).getName();
                will(returnValue("Project"));

                oneOf(transformer).to(project);
                will(returnValue(javaProject));

                oneOf(javaProject).getOutputLocation();
                will(returnValue(outputFolderPath));

                oneOf(outputFolderPath).removeFirstSegments(1);
                will(returnValue(strippedOutputFolderPath));

                oneOf(strippedOutputFolderPath).toOSString();
                will(returnValue(outputFolder));

                oneOf(workingCopy).setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                        "com.technophobia.substeps.runner.runtime.DefinableFeatureTest");
                oneOf(workingCopy).setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "Project");
                oneOf(workingCopy).setAttribute(JUnitLaunchConfigurationConstants.ATTR_KEEPRUNNING, false);
                oneOf(workingCopy).setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, "");
                oneOf(workingCopy).setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_RUNNER_KIND,
                        TestKindRegistry.JUNIT4_TEST_KIND_ID);
                oneOf(workingCopy).setAttribute("com.technophobia.substeps.junit.featureFile", pathString);
                oneOf(workingCopy).setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
                        "-DsubstepsFeatureFile=" + pathString + " -DoutputFolder=" + outputFolder);
            }
        });

        decorator.decorate(workingCopy, resource);
    }
}
