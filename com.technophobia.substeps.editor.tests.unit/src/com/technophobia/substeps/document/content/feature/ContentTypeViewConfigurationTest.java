/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.content.feature;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.colour.ColourManager;
import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.document.content.assist.ContentAssistantFactory;
import com.technophobia.substeps.document.content.view.ContentTypeViewConfiguration;
import com.technophobia.substeps.document.formatting.FormattingContextFactory;

@RunWith(JMock.class)
public class ContentTypeViewConfigurationTest {

    private Mockery context;

    private ColourManager colourManager;
    private ContentTypeDefinitionFactory contentTypeDefinitionFactory;
    private FormattingContextFactory formattingContextFactory;
    private ContentAssistantFactory contentAssistantFactory;

    private ContentTypeViewConfiguration viewConfiguration;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.colourManager = new ColourManager();
        this.contentTypeDefinitionFactory = context.mock(ContentTypeDefinitionFactory.class);
        this.formattingContextFactory = context.mock(FormattingContextFactory.class);
        this.contentAssistantFactory = context.mock(ContentAssistantFactory.class);

        this.viewConfiguration = new ContentTypeViewConfiguration(colourManager, contentTypeDefinitionFactory,
                formattingContextFactory, contentAssistantFactory);
    }


    @Test
    public void canGetConfiguredContentTypesFromDefinitionFactory() {
        final ContentTypeDefinition definition1 = context.mock(ContentTypeDefinition.class, "definition1");
        final ContentTypeDefinition definition2 = context.mock(ContentTypeDefinition.class, "definition2");
        final ContentTypeDefinition definition3 = context.mock(ContentTypeDefinition.class, "definition3");

        context.checking(new Expectations() {
            {
                oneOf(definition1).id();
                will(returnValue("definition-1"));

                oneOf(definition2).id();
                will(returnValue("definition-2"));

                oneOf(definition3).id();
                will(returnValue("definition-3"));

                oneOf(contentTypeDefinitionFactory).contentTypeDefinitions();
                will(returnValue(new ContentTypeDefinition[] { definition1, definition2, definition3 }));
            }
        });

        final String[] contentTypes = viewConfiguration.getConfiguredContentTypes(null);
        assertThat(Arrays.asList(contentTypes), hasItems("definition-1", "definition-2", "definition-3"));
    }

}
