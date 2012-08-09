package com.technophobia.substeps.step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class ProvidedSuggestionManagerTest {

    private Mockery context;
    private Transformer<IResource, IProject> resourceToProjectTransformer;
    private IResource resource;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.resource = context.mock(IResource.class);

        this.resourceToProjectTransformer = context.mock(Transformer.class);
    }


    @Test
    public void noProvidersFindNoSuggestions() {
        final ProvidedSuggestionManager suggestionManager = new ProvidedSuggestionManager(resourceToProjectTransformer);
        final IProject project = context.mock(IProject.class);

        context.checking(new Expectations() {
            {
                oneOf(resourceToProjectTransformer).from(resource);
                will(returnValue(project));
            }
        });

        assertTrue(suggestionManager.suggestionsFor(SuggestionType.FEATURE, resource).isEmpty());
    }


    @Test
    public void singleProviderFindsSuggestions() {
        final ProjectSuggestionProvider provider = context.mock(ProjectSuggestionProvider.class);
        final IProject project = context.mock(IProject.class);
        final Collection<String> suggestions = Arrays.asList("suggestion-1", "suggestion-2", "suggestion-3");

        context.checking(new Expectations() {
            {
                oneOf(resourceToProjectTransformer).from(resource);
                will(returnValue(project));

                oneOf(provider).suggestionsFor(project);
                will(returnValue(suggestions));
            }
        });

        final ProvidedSuggestionManager suggestionManager = new ProvidedSuggestionManager(resourceToProjectTransformer);
        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, provider);

        assertThat(suggestionManager.suggestionsFor(SuggestionType.FEATURE, resource),
                hasItems(suggestions.toArray(new String[suggestions.size()])));
    }


    @Test
    public void multiProviderFindsCombinedSuggestions() {
        final ProjectSuggestionProvider provider1 = context.mock(ProjectSuggestionProvider.class, "provider1");
        final ProjectSuggestionProvider provider2 = context.mock(ProjectSuggestionProvider.class, "provider2");
        final IProject project = context.mock(IProject.class);
        final Collection<String> suggestions1 = Arrays.asList("suggestion-11", "suggestion-12", "suggestion-13");
        final Collection<String> suggestions2 = Arrays.asList("suggestion-21", "suggestion-22", "suggestion-23");

        context.checking(new Expectations() {
            {
                oneOf(resourceToProjectTransformer).from(resource);
                will(returnValue(project));

                oneOf(provider1).suggestionsFor(project);
                will(returnValue(suggestions1));
                oneOf(provider2).suggestionsFor(project);
                will(returnValue(suggestions2));
            }
        });

        final ProvidedSuggestionManager suggestionManager = new ProvidedSuggestionManager(resourceToProjectTransformer);
        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, provider1);
        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, provider2);

        final Collection<String> expectedSuggestions = Arrays.asList("suggestion-11", "suggestion-12", "suggestion-13",
                "suggestion-21", "suggestion-22", "suggestion-23");
        assertThat(suggestionManager.suggestionsFor(SuggestionType.FEATURE, resource),
                hasItems(expectedSuggestions.toArray(new String[expectedSuggestions.size()])));
    }


    @Test
    public void multiProviderProducesNoDuplicateSuggestions() {
        final ProjectSuggestionProvider provider1 = context.mock(ProjectSuggestionProvider.class, "provider1");
        final ProjectSuggestionProvider provider2 = context.mock(ProjectSuggestionProvider.class, "provider2");
        final IProject project = context.mock(IProject.class);
        final Collection<String> suggestions1 = Arrays.asList("suggestion-1", "suggestion-2", "suggestion-3");
        final Collection<String> suggestions2 = Arrays.asList("suggestion-1", "suggestion-4", "suggestion-3");

        context.checking(new Expectations() {
            {
                oneOf(resourceToProjectTransformer).from(resource);
                will(returnValue(project));

                oneOf(provider1).suggestionsFor(project);
                will(returnValue(suggestions1));
                oneOf(provider2).suggestionsFor(project);
                will(returnValue(suggestions2));
            }
        });

        final ProvidedSuggestionManager suggestionManager = new ProvidedSuggestionManager(resourceToProjectTransformer);
        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, provider1);
        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, provider2);

        final Collection<String> expectedSuggestions = Arrays.asList("suggestion-1", "suggestion-2", "suggestion-3",
                "suggestion-4");
        assertThat(suggestionManager.suggestionsFor(SuggestionType.FEATURE, resource),
                hasItems(expectedSuggestions.toArray(new String[expectedSuggestions.size()])));
    }


    @Test
    public void providerIsIgnoredIfSourceIsNotSupported() {
        final ProjectSuggestionProvider provider1 = context.mock(ProjectSuggestionProvider.class, "provider1");
        final ProjectSuggestionProvider provider2 = context.mock(ProjectSuggestionProvider.class, "provider2");
        final IProject project = context.mock(IProject.class);
        final Collection<String> suggestions2 = Arrays.asList("suggestion-21", "suggestion-22", "suggestion-23");

        context.checking(new Expectations() {
            {
                oneOf(resourceToProjectTransformer).from(resource);
                will(returnValue(project));

                oneOf(provider2).suggestionsFor(project);
                will(returnValue(suggestions2));
            }
        });

        final ProvidedSuggestionManager suggestionManager = new ProvidedSuggestionManager(resourceToProjectTransformer);
        suggestionManager.addProvider(SuggestionSource.SUBSTEP_DEFINITION, provider1);
        suggestionManager.addProvider(SuggestionSource.PROJECT_STEP_IMPLEMENTATION, provider2);

        final Collection<String> expectedSuggestions = Arrays.asList("suggestion-21", "suggestion-22", "suggestion-23");
        assertThat(suggestionManager.suggestionsFor(SuggestionType.SUBSTEP, resource),
                hasItems(expectedSuggestions.toArray(new String[expectedSuggestions.size()])));
    }
}
