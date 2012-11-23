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
