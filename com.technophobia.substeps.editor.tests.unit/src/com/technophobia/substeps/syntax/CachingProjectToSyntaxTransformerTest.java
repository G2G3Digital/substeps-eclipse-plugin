package com.technophobia.substeps.syntax;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.eclipse.core.resources.IProject;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.log.PluginLogger;
import com.technophobia.substeps.model.Syntax;
import com.technophobia.substeps.supplier.CachingResultTransformer;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class CachingProjectToSyntaxTransformerTest {

    private Mockery context;

    private Transformer<IProject, Syntax> delegate;
    private CachingResultTransformer<IProject, Syntax> transformer;

    private PluginLogger pluginLogger;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.delegate = context.mock(Transformer.class);
        this.pluginLogger = context.mock(PluginLogger.class);
        this.transformer = new CachingProjectToSyntaxTransformer(delegate, pluginLogger);
    }


    @Test
    public void retrieveSyntaxFetchesFromDelegateIfUnknown() {

        final IProject project = context.mock(IProject.class);
        final Syntax syntax = createSyntax();

        context.checking(new Expectations() {
            {
                oneOf(delegate).from(project);
                will(returnValue(syntax));

                allowing(project).getName();
                will(returnValue("Project"));

                allowing(pluginLogger).info(with(any(String.class)));
            }
        });

        assertThat(transformer.from(project), is(syntax));
    }


    @Test
    public void retrieveSyntaxFetchesFromCacheIfKnown() {
        final IProject project = context.mock(IProject.class);
        final Syntax syntax = createSyntax();

        context.checking(new Expectations() {
            {
                oneOf(delegate).from(project);
                will(returnValue(syntax));

                allowing(project).getName();
                will(returnValue("Project"));

                allowing(pluginLogger).info(with(any(String.class)));
            }
        });

        assertThat(transformer.from(project), is(syntax));
        assertThat(transformer.from(project), is(syntax));
    }


    @Test
    public void retrieveSyntaxFetchesFromDelegateIfDirty() {
        final IProject project = context.mock(IProject.class);
        final Syntax syntax = createSyntax();

        context.checking(new Expectations() {
            {
                exactly(2).of(delegate).from(project);
                will(returnValue(syntax));

                allowing(project).getName();
                will(returnValue("Project"));

                allowing(pluginLogger).info(with(any(String.class)));
            }
        });

        assertThat(transformer.from(project), is(syntax));

        transformer.refreshCacheFor(project);

        assertThat(transformer.from(project), is(syntax));
    }


    private Syntax createSyntax() {
        return new Syntax();
    }
}
