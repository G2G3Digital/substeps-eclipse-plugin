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

        assertTrue(suggestionManager.suggestionsFor(resource).isEmpty());
    }


    @Test
    public void singleProviderFindsSuggestions() {
        final ProjectSuggestionProvider provider = context.mock(ProjectSuggestionProvider.class);
        final IProject project = context.mock(IProject.class);
        final Collection<Suggestion> suggestions = Arrays.asList(new Suggestion("suggestion-1"), new Suggestion(
                "suggestion-2"), new Suggestion("suggestion-3"));

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

        assertThat(suggestionManager.suggestionsFor(resource),
                hasItems(suggestions.toArray(new Suggestion[suggestions.size()])));
    }


    @Test
    public void multiProviderFindsCombinedSuggestions() {
        final ProjectSuggestionProvider provider1 = context.mock(ProjectSuggestionProvider.class, "provider1");
        final ProjectSuggestionProvider provider2 = context.mock(ProjectSuggestionProvider.class, "provider2");
        final IProject project = context.mock(IProject.class);
        final Collection<Suggestion> suggestions1 = Arrays.asList(new Suggestion("suggestion-11"), new Suggestion(
                "suggestion-12"), new Suggestion("suggestion-13"));
        final Collection<Suggestion> suggestions2 = Arrays.asList(new Suggestion("suggestion-21"), new Suggestion(
                "suggestion-22"), new Suggestion("suggestion-23"));

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

        final Collection<Suggestion> expectedSuggestions = Arrays.asList(new Suggestion("suggestion-11"),
                new Suggestion("suggestion-12"), new Suggestion("suggestion-13"), new Suggestion("suggestion-21"),
                new Suggestion("suggestion-22"), new Suggestion("suggestion-23"));
        assertThat(suggestionManager.suggestionsFor(resource),
                hasItems(expectedSuggestions.toArray(new Suggestion[expectedSuggestions.size()])));
    }


    @Test
    public void multiProviderProducesNoDuplicateSuggestions() {
        final ProjectSuggestionProvider provider1 = context.mock(ProjectSuggestionProvider.class, "provider1");
        final ProjectSuggestionProvider provider2 = context.mock(ProjectSuggestionProvider.class, "provider2");
        final IProject project = context.mock(IProject.class);
        final Collection<Suggestion> suggestions1 = Arrays.asList(new Suggestion("suggestion-1"), new Suggestion(
                "suggestion-2"), new Suggestion("suggestion-3"));
        final Collection<Suggestion> suggestions2 = Arrays.asList(new Suggestion("suggestion-1"), new Suggestion(
                "suggestion-4"), new Suggestion("suggestion-3"));

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

        final Collection<Suggestion> expectedSuggestions = Arrays.asList(new Suggestion("suggestion-1"),
                new Suggestion("suggestion-2"), new Suggestion("suggestion-3"), new Suggestion("suggestion-4"));
        assertThat(suggestionManager.suggestionsFor(resource),
                hasItems(expectedSuggestions.toArray(new Suggestion[expectedSuggestions.size()])));
    }
}
