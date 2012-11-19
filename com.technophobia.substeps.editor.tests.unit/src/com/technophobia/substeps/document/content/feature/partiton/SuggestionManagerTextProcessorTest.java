package com.technophobia.substeps.document.content.feature.partiton;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.step.ContextualSuggestionManager;
import com.technophobia.substeps.step.Suggestion;
import com.technophobia.substeps.supplier.Supplier;

@RunWith(JMock.class)
public class SuggestionManagerTextProcessorTest {

    private static final IToken token = new Token("Test");

    private static final Collection<Suggestion> suggestions = Arrays.asList(new Suggestion("suggestion-1"),
            new Suggestion("suggestion-2"), new Suggestion("suggestion-3"), new Suggestion("valid-suggestion"));

    private Mockery context;

    private Supplier<ContextualSuggestionManager> suggestionManagerSupplier;
    private Supplier<IProject> projectSupplier;

    private ContextualSuggestionManager suggestionManager;
    private IProject project;

    private TextProcessor<IToken> textProcessor;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.projectSupplier = context.mock(Supplier.class, "projectSupplier");
        this.suggestionManagerSupplier = context.mock(Supplier.class, "suggestionManagerSupplier");

        this.suggestionManager = context.mock(ContextualSuggestionManager.class);
        this.project = context.mock(IProject.class);

        this.textProcessor = new SuggestionManagerTextProcessor(token, projectSupplier, suggestionManagerSupplier);
    }


    @Test
    public void nullProjectReturnsNullToken() {
        final String text = "invalid suggestion";

        context.checking(new Expectations() {
            {
                oneOf(projectSupplier).get();
                will(returnValue(null));
            }
        });

        assertThat(textProcessor.doWithText(text), is(nullValue()));
    }


    @Test
    public void returnsNullIfNoMatchingSuggestionsFound() {
        final String text = "invalid suggestion";

        context.checking(new Expectations() {
            {
                oneOf(projectSupplier).get();
                will(returnValue(project));

                oneOf(suggestionManagerSupplier).get();
                will(returnValue(suggestionManager));

                oneOf(suggestionManager).suggestionsFor(project);
                will(returnValue(suggestions));
            }
        });

        assertThat(textProcessor.doWithText(text), is(nullValue()));
    }


    @Test
    public void returnsSuccessTokenIfSuggestionFound() {
        final String text = "valid-suggestion";

        context.checking(new Expectations() {
            {
                oneOf(projectSupplier).get();
                will(returnValue(project));

                oneOf(suggestionManagerSupplier).get();
                will(returnValue(suggestionManager));

                oneOf(suggestionManager).suggestionsFor(project);
                will(returnValue(suggestions));
            }
        });

        assertThat(textProcessor.doWithText(text), is(token));
    }

}
