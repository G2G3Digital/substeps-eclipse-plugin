package com.technophobia.eclipse.project;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.preference.PreferenceLookup;
import com.technophobia.eclipse.preference.PreferenceLookupFactory;
import com.technophobia.substeps.preferences.SubstepsPreferences;

@RunWith(JMock.class)
public class PropertyBasedProjectManagerTest {

    private Mockery context;

    private PreferenceLookupFactory<IProject> preferenceLookupFactory;
    private PreferenceLookup preferenceLookup;

    private ProjectManager projectManager;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.preferenceLookupFactory = context.mock(PreferenceLookupFactory.class);
        this.preferenceLookup = context.mock(PreferenceLookup.class);

        this.projectManager = new PropertyBasedProjectManager(preferenceLookupFactory);
    }


    @Test
    public void findsFeatureFolderWhenSet() {
        final IProject project = context.mock(IProject.class);
        final IFolder folder = context.mock(IFolder.class);
        final IPath path = context.mock(IPath.class);

        context.checking(new Expectations() {
            {
                oneOf(preferenceLookupFactory).preferencesFor(project);
                will(returnValue(preferenceLookup));

                oneOf(preferenceLookup).valueFor(SubstepsPreferences.FEATURE_FOLDER.key());
                will(returnValue("feature"));

                oneOf(project).getFolder("feature");
                will(returnValue(folder));

                oneOf(folder).getLocation();
                will(returnValue(path));
            }
        });

        final IPath p = projectManager.featureFolderFor(project);
        assertThat(p, is(path));
    }


    @Test
    public void returnsProjectFolderWhenFeatureFolderNotSet() {
        final IProject project = context.mock(IProject.class);
        final IPath path = context.mock(IPath.class);

        context.checking(new Expectations() {
            {
                oneOf(preferenceLookupFactory).preferencesFor(project);
                will(returnValue(preferenceLookup));

                oneOf(preferenceLookup).valueFor(SubstepsPreferences.FEATURE_FOLDER.key());
                will(returnValue(null));

                oneOf(project).getLocation();
                will(returnValue(path));
            }
        });

        final IPath p = projectManager.featureFolderFor(project);
        assertThat(p, is(path));
    }


    @Test
    public void findsSubstepsFolderWhenSet() {
        final IProject project = context.mock(IProject.class);
        final IFolder folder = context.mock(IFolder.class);
        final IPath path = context.mock(IPath.class);

        context.checking(new Expectations() {
            {
                oneOf(preferenceLookupFactory).preferencesFor(project);
                will(returnValue(preferenceLookup));

                oneOf(preferenceLookup).valueFor(SubstepsPreferences.SUBSTEPS_FOLDER.key());
                will(returnValue("substeps"));

                oneOf(project).getFolder("substeps");
                will(returnValue(folder));

                oneOf(folder).getLocation();
                will(returnValue(path));
            }
        });

        final IPath p = projectManager.substepsFolderFor(project);
        assertThat(p, is(path));
    }


    @Test
    public void returnsProjectFolderWhenSubstepsFolderNotSet() {
        final IProject project = context.mock(IProject.class);
        final IPath path = context.mock(IPath.class);

        context.checking(new Expectations() {
            {
                oneOf(preferenceLookupFactory).preferencesFor(project);
                will(returnValue(preferenceLookup));

                oneOf(preferenceLookup).valueFor(SubstepsPreferences.SUBSTEPS_FOLDER.key());
                will(returnValue(null));

                oneOf(project).getLocation();
                will(returnValue(path));
            }
        });

        final IPath p = projectManager.substepsFolderFor(project);
        assertThat(p, is(path));
    }
}
