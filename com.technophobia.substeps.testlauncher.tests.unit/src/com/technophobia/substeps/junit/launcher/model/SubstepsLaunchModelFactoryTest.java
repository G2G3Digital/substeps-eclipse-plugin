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
package com.technophobia.substeps.junit.launcher.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.launcher.config.SubstepsLaunchConfigurationConstants;
import com.technophobia.substeps.junit.launcher.SubstepsFeatureLaunchShortcut;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class SubstepsLaunchModelFactoryTest {

    private Mockery context;

    private LaunchModelFactory factory;

    private Transformer<IProject, String> substepsFolderLocator;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.substepsFolderLocator = context.mock(Transformer.class);

        this.factory = new SubstepsLaunchModelFactory(substepsFolderLocator);
    }


    @Test
    public void createFromResourceReturnsPopulatedSubstepsLaunchModel() {
        final IResource resource = context.mock(IResource.class);
        final IProject project = context.mock(IProject.class);
        final IPath fullPath = context.mock(IPath.class, "fullPath");
        final IPath strippedPath = context.mock(IPath.class, "strippedPath");

        context.checking(new Expectations() {
            {
                oneOf(resource).getProject();
                will(returnValue(project));

                oneOf(project).getName();
                will(returnValue("a-project"));

                oneOf(resource).getFullPath();
                will(returnValue(fullPath));

                oneOf(fullPath).removeFirstSegments(1);
                will(returnValue(strippedPath));

                oneOf(strippedPath).toOSString();
                will(returnValue("/feature"));

                oneOf(substepsFolderLocator).from(project);
                will(returnValue("/substeps"));
            }
        });

        final LaunchModel model = factory.createFrom(resource);
        assertThat(model, is(SubstepsLaunchModel.class));

        final SubstepsLaunchModel substepsLaunchModel = (SubstepsLaunchModel) model;
        assertThat(substepsLaunchModel.getProjectName(), is("a-project"));
        assertThat(substepsLaunchModel.getFeatureFile(), is("/feature"));
        assertThat(substepsLaunchModel.getSubstepsFile(), is("/substeps"));
    }


    @Test
    public void createFromLaunchConfigReturnsPopulatesSubstepsLaunchModel() throws Exception {
        final ILaunchConfiguration config = context.mock(ILaunchConfiguration.class);

        context.checking(new Expectations() {
            {
                oneOf(config).getAttribute(SubstepsLaunchConfigurationConstants.ATTR_FEATURE_PROJECT, "");
                will(returnValue("a-project"));

                oneOf(config).getAttribute(SubstepsFeatureLaunchShortcut.ATTR_FEATURE_FILE, "");
                will(returnValue("/feature"));

                oneOf(config).getAttribute(SubstepsLaunchConfigurationConstants.ATTR_SUBSTEPS_FILE, "");
                will(returnValue("/substeps"));
            }
        });

        final LaunchModel model = factory.createFrom(config);
        assertThat(model, is(SubstepsLaunchModel.class));

        final SubstepsLaunchModel substepsLaunchModel = (SubstepsLaunchModel) model;
        assertThat(substepsLaunchModel.getProjectName(), is("a-project"));
        assertThat(substepsLaunchModel.getFeatureFile(), is("/feature"));
        assertThat(substepsLaunchModel.getSubstepsFile(), is("/substeps"));
    }
}
