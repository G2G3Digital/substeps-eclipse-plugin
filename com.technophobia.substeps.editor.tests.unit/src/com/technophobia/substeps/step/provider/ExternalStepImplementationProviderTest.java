package com.technophobia.substeps.step.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jdt.core.JavaCore;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.glossary.StepImplementationsDescriptor;
import com.technophobia.substeps.step.Suggestion;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class ExternalStepImplementationProviderTest {

    private Mockery context;

    private ExternalStepImplementationProvider suggestionProvider;

    private Transformer<IProject, List<StepImplementationsDescriptor>> stepImplementationLoader;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.stepImplementationLoader = context.mock(Transformer.class);

        this.suggestionProvider = new ExternalStepImplementationProvider(stepImplementationLoader);
    }


    @Test
    public void noStepImplementationsForProjectYieldsNoSuggestions() throws Exception {
        final IProject project = aProjectWith("project");
        suggestionProvider.load(aWorkspaceWith(project));

        final Collection<Suggestion> suggestions = suggestionProvider.suggestionsFor(project);
        assertTrue(suggestions.isEmpty());
    }


    @Test
    public void stepImplementationsForProjectYieldsCorrespondingSuggestions() throws Exception {
        final IProject project1 = aProjectWith("project-1",
                aStepImplementation("stepClass1Project1", "step1", "step2"),
                aStepImplementation("stepClass2Project1", "step4", "step4"));
        final IProject project2 = aProjectWith("project-2",
                aStepImplementation("stepClass1Project2", "step5", "step6"),
                aStepImplementation("stepClass2Project2", "step7", "step8"));
        suggestionProvider.load(aWorkspaceWith(project1, project2));

        final Collection<Suggestion> suggestions = suggestionProvider.suggestionsFor(project2);
        assertEquals(4, suggestions.size());
        assertThat(
                suggestions,
                hasItems(new Suggestion("step5"), new Suggestion("step6"), new Suggestion("step7"), new Suggestion(
                        "step8")));

    }


    @Test
    public void clearingProjectRemovesCorrespondingSuggestions() throws Exception {
        final IProject project1 = aProjectWith("project-1",
                aStepImplementation("stepClass1Project1", "step1", "step2"),
                aStepImplementation("stepClass2Project1", "step4", "step4"));
        final IProject project2 = aProjectWith("project-2",
                aStepImplementation("stepClass1Project2", "step5", "step6"),
                aStepImplementation("stepClass2Project2", "step7", "step8"));
        suggestionProvider.load(aWorkspaceWith(project1, project2));

        final Collection<Suggestion> suggestions = suggestionProvider.suggestionsFor(project2);
        assertEquals(4, suggestions.size());
        assertThat(
                suggestions,
                hasItems(new Suggestion("step5"), new Suggestion("step6"), new Suggestion("step7"), new Suggestion(
                        "step8")));

        suggestionProvider.clearStepImplementationsFor(project2);
        final Collection<Suggestion> updatedSuggestions = suggestionProvider.suggestionsFor(project2);
        assertTrue(updatedSuggestions.isEmpty());
    }


    @Test
    public void addingDependencyAddsCorrespondingSuggestions() throws Exception {
        final IProject project1 = aProjectWith("project-1",
                aStepImplementation("stepClass1Project1", "step1", "step2"),
                aStepImplementation("stepClass2Project1", "step4", "step4"));
        final IProject project2 = aProjectWith("project-2");
        suggestionProvider.load(aWorkspaceWith(project1, project2));

        final Collection<Suggestion> suggestions = suggestionProvider.suggestionsFor(project2);
        assertTrue(suggestions.isEmpty());

        context.checking(new Expectations() {
            {
                oneOf(stepImplementationLoader).from(project2);
                will(returnValue(Arrays.asList(aStepImplementation("stepClass1Project2", "step5", "step6"),
                        aStepImplementation("stepClass2Project2", "step7", "step8"))));
            }
        });

        suggestionProvider.loadProject(project2);
        final Collection<Suggestion> updatedSuggestions = suggestionProvider.suggestionsFor(project2);
        assertEquals(4, updatedSuggestions.size());
        assertThat(
                updatedSuggestions,
                hasItems(new Suggestion("step5"), new Suggestion("step6"), new Suggestion("step7"), new Suggestion(
                        "step8")));
    }


    @Test
    public void closedProjectDoesntLoadSuggestions() {
        final IProject project = context.mock(IProject.class);
        context.checking(new Expectations() {
            {
                oneOf(project).isAccessible();
                will(returnValue(Boolean.FALSE));
            }
        });

        suggestionProvider.load(aWorkspaceWith(project));
        final Collection<Suggestion> suggestions = suggestionProvider.suggestionsFor(project);
        assertTrue(suggestions.isEmpty());
    }


    @Test
    public void ProjectDoesntLoadSuggestions() throws Exception {
        final IProject project = context.mock(IProject.class);
        context.checking(new Expectations() {
            {
                oneOf(project).isAccessible();
                will(returnValue(Boolean.TRUE));

                oneOf(project).hasNature(JavaCore.NATURE_ID);
                will(returnValue(Boolean.FALSE));
            }
        });

        suggestionProvider.load(aWorkspaceWith(project));
        final Collection<Suggestion> suggestions = suggestionProvider.suggestionsFor(project);
        assertTrue(suggestions.isEmpty());
    }


    @Test
    public void noStepImplementationsForProjectYieldsNoStepImplementationClasses() throws Exception {
        final IProject project = aProjectWith("project");
        suggestionProvider.load(aWorkspaceWith(project));

        final Collection<String> stepImplementations = suggestionProvider.stepImplementationClasses(project);
        assertTrue(stepImplementations.isEmpty());
    }


    @Test
    public void stepImplementationsForProjectYieldsCorrespondingStepImplementationClasses() throws Exception {
        final IProject project1 = aProjectWith("project-1",
                aStepImplementation("stepClass1Project1", "step1", "step2"),
                aStepImplementation("stepClass2Project1", "step4", "step4"));
        final IProject project2 = aProjectWith("project-2",
                aStepImplementation("stepClass1Project2", "step5", "step6"),
                aStepImplementation("stepClass2Project2", "step7", "step8"));
        suggestionProvider.load(aWorkspaceWith(project1, project2));

        final Collection<String> stepImplementations = suggestionProvider.stepImplementationClasses(project2);
        assertEquals(2, stepImplementations.size());
        assertThat(stepImplementations, hasItems("stepClass1Project2", "stepClass2Project2"));

    }


    @Test
    public void clearingProjectRemovesCorrespondingStepImplementationClasses() throws Exception {
        final IProject project1 = aProjectWith("project-1",
                aStepImplementation("stepClass1Project1", "step1", "step2"),
                aStepImplementation("stepClass2Project1", "step4", "step4"));
        final IProject project2 = aProjectWith("project-2",
                aStepImplementation("stepClass1Project2", "step5", "step6"),
                aStepImplementation("stepClass2Project2", "step7", "step8"));
        suggestionProvider.load(aWorkspaceWith(project1, project2));

        final Collection<String> stepImplementations = suggestionProvider.stepImplementationClasses(project2);
        assertEquals(2, stepImplementations.size());
        assertThat(stepImplementations, hasItems("stepClass1Project2", "stepClass2Project2"));

        suggestionProvider.clearStepImplementationsFor(project2);
        final Collection<String> updatedStepImplementations = suggestionProvider.stepImplementationClasses(project2);
        assertTrue(updatedStepImplementations.isEmpty());
    }


    @Test
    public void addingDependencyAddsCorrespondingStepImplementationClasses() throws Exception {
        final IProject project1 = aProjectWith("project-1",
                aStepImplementation("stepClass1Project1", "step1", "step2"),
                aStepImplementation("stepClass2Project1", "step4", "step4"));
        final IProject project2 = aProjectWith("project-2");
        suggestionProvider.load(aWorkspaceWith(project1, project2));

        final Collection<String> stepImplementations = suggestionProvider.stepImplementationClasses(project2);
        assertTrue(stepImplementations.isEmpty());

        context.checking(new Expectations() {
            {
                oneOf(stepImplementationLoader).from(project2);
                will(returnValue(Arrays.asList(aStepImplementation("stepClass1Project2", "step5", "step6"),
                        aStepImplementation("stepClass2Project2", "step7", "step8"))));
            }
        });

        suggestionProvider.loadProject(project2);
        final Collection<String> updatedStepImplementations = suggestionProvider.stepImplementationClasses(project2);
        assertEquals(updatedStepImplementations.size(), 2);
        assertThat(updatedStepImplementations, hasItems("stepClass1Project2", "stepClass2Project2"));
    }


    private IWorkspace aWorkspaceWith(final IProject... projects) {
        final IWorkspace workspace = context.mock(IWorkspace.class);
        final IWorkspaceRoot root = context.mock(IWorkspaceRoot.class);

        context.checking(new Expectations() {
            {
                oneOf(workspace).getRoot();
                will(returnValue(root));

                oneOf(root).getProjects();
                will(returnValue(projects));
            }
        });

        return workspace;
    }


    private IProject aProjectWith(final String name, final StepImplementationsDescriptor... stepImplementations)
            throws Exception {
        final IProject project = context.mock(IProject.class, name);

        context.checking(new Expectations() {
            {
                oneOf(stepImplementationLoader).from(project);
                will(returnValue(Arrays.asList(stepImplementations)));

                oneOf(project).isAccessible();
                will(returnValue(Boolean.TRUE));

                oneOf(project).hasNature(JavaCore.NATURE_ID);
                will(returnValue(Boolean.TRUE));
            }
        });

        return project;
    }


    private StepImplementationsDescriptor aStepImplementation(final String className, final String... stepExpressions) {
        final StepImplementationsDescriptor stepImplementations = new StepImplementationsDescriptor(className);
        for (final String stepExpression : stepExpressions) {
            final StepDescriptor stepDescriptor = new StepDescriptor();
            stepDescriptor.setExpression(stepExpression);
            stepImplementations.addStepTags(stepDescriptor);
        }
        return stepImplementations;
    }
}
