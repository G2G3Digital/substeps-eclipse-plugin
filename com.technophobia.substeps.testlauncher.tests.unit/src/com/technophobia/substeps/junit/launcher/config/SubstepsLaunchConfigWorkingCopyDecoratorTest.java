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
package com.technophobia.substeps.junit.launcher.config;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.transformer.Decorator;
import com.technophobia.substeps.junit.launcher.model.LaunchModel;
import com.technophobia.substeps.junit.launcher.model.LaunchModelFactory;

@RunWith(JMock.class)
public class SubstepsLaunchConfigWorkingCopyDecoratorTest {

    private Mockery context;

    private IResource resource;

    private LaunchModelFactory launchModelFactory;

    private Decorator<ILaunchConfigurationWorkingCopy, IResource> decorator;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.resource = context.mock(IResource.class);
        this.launchModelFactory = context.mock(LaunchModelFactory.class);

        this.decorator = new SubstepsLaunchConfigWorkingCopyDecorator(launchModelFactory);
    }


    @Test
    public void canDecorate() {
        final ILaunchConfigurationWorkingCopy workingCopy = context.mock(ILaunchConfigurationWorkingCopy.class);

        final LaunchModel launchModel = context.mock(LaunchModel.class);

        context.checking(new Expectations() {
            {
                oneOf(launchModelFactory).createFrom(resource);
                will(returnValue(launchModel));

                oneOf(launchModel).saveTo(workingCopy);
            }
        });

        decorator.decorate(workingCopy, resource);
    }

    // @Test
    // public void canDecorate() {
    //
    // final ILaunchConfigurationWorkingCopy workingCopy =
    // context.mock(ILaunchConfigurationWorkingCopy.class);
    // final String pathString = "/file/path";
    // final IPath resourcePath = context.mock(IPath.class, "resource");
    //
    // context.checking(new Expectations() {
    // {
    // oneOf(resource).getProject();
    // will(returnValue(project));
    //
    // oneOf(resource).getFullPath();
    // will(returnValue(resourcePath));
    //
    // oneOf(resourcePath).removeFirstSegments(1);
    // will(returnValue(resourcePath));
    //
    // oneOf(resourcePath).toOSString();
    // will(returnValue(pathString));
    //
    // exactly(2).of(project).getName();
    // will(returnValue("Project"));
    //
    // oneOf(projectToSubstepsFolderTransformer).from(project);
    // will(returnValue("substeps"));
    //
    // oneOf(workingCopy).setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
    // "com.technophobia.substeps.runner.runtime.DefinableFeatureTest");
    // oneOf(workingCopy).setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
    // "Project");
    // oneOf(workingCopy).setAttribute(SubstepsLaunchConfigurationConstants.ATTR_FEATURE_PROJECT,
    // "Project");
    // oneOf(workingCopy).setAttribute(SubstepsLaunchConfigurationConstants.ATTR_KEEPRUNNING,
    // false);
    // oneOf(workingCopy).setAttribute(SubstepsLaunchConfigurationConstants.ATTR_TEST_CONTAINER,
    // "");
    // oneOf(workingCopy).setAttribute(SubstepsLaunchConfigurationConstants.ATTR_TEST_RUNNER_KIND,
    // TestKindRegistry.JUNIT4_TEST_KIND_ID);
    // oneOf(workingCopy).setAttribute("com.technophobia.substeps.junit.featureFile",
    // pathString);
    // oneOf(workingCopy).setAttribute(SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE,
    // "substeps");
    // oneOf(workingCopy).setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
    // "-DsubstepsFeatureFile=" + pathString);
    // }
    // });
    //
    // decorator.decorate(workingCopy, resource);
    // }
}
