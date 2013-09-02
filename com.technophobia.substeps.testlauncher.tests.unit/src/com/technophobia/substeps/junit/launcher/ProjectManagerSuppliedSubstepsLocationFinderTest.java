package com.technophobia.substeps.junit.launcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class ProjectManagerSuppliedSubstepsLocationFinderTest {

    private Mockery context;

    private ProjectManager projectManager;
    private Supplier<ProjectManager> projectManagerSupplier;

    private Transformer<IProject, String> substepsLocationFinder;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.projectManager = context.mock(ProjectManager.class);
        this.projectManagerSupplier = context.mock(Supplier.class);

        this.substepsLocationFinder = new ProjectManagerSuppliedSubstepsLocationFinder(projectManagerSupplier);
    }


    @Test
    public void canGetSubstepsLocationFromProjectManager() {

        final String projectLocation = "project location";
        final IPath projectPath = context.mock(IPath.class, "projectPath");
        final IProject project = context.mock(IProject.class);

        final String substepsLocation = "substeps location";
        final IPath substepsPath = context.mock(IPath.class, "substepsPath");

        context.checking(new Expectations() {
            {
                oneOf(projectManagerSupplier).get();
                will(returnValue(projectManager));

                oneOf(projectManager).substepsFolderFor(project);
                will(returnValue(substepsPath));

                oneOf(project).getLocation();
                will(returnValue(projectPath));

                oneOf(projectPath).isPrefixOf(substepsPath);
                will(returnValue(true));

                oneOf(projectPath).toOSString();
                will(returnValue(projectLocation));

                oneOf(substepsPath).toOSString();
                will(returnValue(projectLocation + "/" + substepsLocation));
            }
        });

        assertThat(substepsLocationFinder.from(project), is(substepsLocation));
    }


    @Test
    public void returnsNullIfSubstepsPathIsNotChildOfProjectPath() {
        final IPath projectPath = context.mock(IPath.class, "projectPath");
        final IProject project = context.mock(IProject.class);

        final IPath substepsPath = context.mock(IPath.class, "substepsPath");

        context.checking(new Expectations() {
            {
                oneOf(projectManagerSupplier).get();
                will(returnValue(projectManager));

                oneOf(projectManager).substepsFolderFor(project);
                will(returnValue(substepsPath));

                oneOf(project).getLocation();
                will(returnValue(projectPath));

                oneOf(projectPath).isPrefixOf(substepsPath);
                will(returnValue(false));
            }
        });

        assertThat(substepsLocationFinder.from(project), is(nullValue()));
    }
}
