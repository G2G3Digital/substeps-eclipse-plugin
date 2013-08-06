package com.technophobia.substeps.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.project.ProjectManager;
import com.technophobia.substeps.supplier.Predicate;

@RunWith(JMock.class)
public class IsFeatureFolderPredicateTest {

    private Mockery context;

    private ProjectManager projectManager;

    private Predicate<IFolder> predicate;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.projectManager = context.mock(ProjectManager.class);
        this.predicate = new IsFeatureFolderPredicate(projectManager);
    }


    @Test
    public void returnsTrueIfFeatureFile() {

        final IFolder folder = context.mock(IFolder.class);
        final IPath folderPath = context.mock(IPath.class, "folderPath");

        final IProject project = context.mock(IProject.class);
        final IPath featureFolderPath = context.mock(IPath.class, "featureFolderPath");

        context.checking(new Expectations() {
            {
                oneOf(folder).getProject();
                will(returnValue(project));

                oneOf(projectManager).featureFolderFor(project);
                will(returnValue(featureFolderPath));

                oneOf(folder).getFullPath();
                will(returnValue(folderPath));

                oneOf(featureFolderPath).isPrefixOf(folderPath);
                will(returnValue(true));
            }
        });

        assertTrue(predicate.forModel(folder));
    }


    @Test
    public void returnsFalseIfNotFeatureFile() {
        final IFolder folder = context.mock(IFolder.class);
        final IPath folderPath = context.mock(IPath.class, "folderPath");

        final IProject project = context.mock(IProject.class);
        final IPath featureFolderPath = context.mock(IPath.class, "featureFolderPath");

        context.checking(new Expectations() {
            {
                oneOf(folder).getProject();
                will(returnValue(project));

                oneOf(projectManager).featureFolderFor(project);
                will(returnValue(featureFolderPath));

                oneOf(folder).getFullPath();
                will(returnValue(folderPath));

                oneOf(featureFolderPath).isPrefixOf(folderPath);
                will(returnValue(false));
            }
        });

        assertFalse(predicate.forModel(folder));
    }


    @Test
    public void returnsFalseIfProjectDoesNotHaveFeatureFolder() {
        final IFolder folder = context.mock(IFolder.class);

        final IProject project = context.mock(IProject.class);

        context.checking(new Expectations() {
            {
                oneOf(folder).getProject();
                will(returnValue(project));

                oneOf(projectManager).featureFolderFor(project);
                will(returnValue(null));
            }
        });

        assertFalse(predicate.forModel(folder));
    }
}
