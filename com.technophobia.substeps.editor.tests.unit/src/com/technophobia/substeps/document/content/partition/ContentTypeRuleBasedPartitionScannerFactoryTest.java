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
package com.technophobia.substeps.document.content.partition;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.document.content.ContentTypeDefinition;
import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;

@RunWith(JMock.class)
public class ContentTypeRuleBasedPartitionScannerFactoryTest {

    private Mockery context;

    private ContentTypeDefinitionFactory contentTypeDefinitionFactory;

    private ContentTypeRuleBasedPartitionScannerFactory partitionScannerFactory;


    @Before
    public void initialiseFactory() {
        this.context = new Mockery();

        this.contentTypeDefinitionFactory = context.mock(ContentTypeDefinitionFactory.class);

        this.partitionScannerFactory = new ContentTypeRuleBasedPartitionScannerFactory(contentTypeDefinitionFactory);
    }


    @Test
    public void canCreateLegalContentTypesFromDefinitionIds() {

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

        assertThat(Arrays.asList(partitionScannerFactory.legalContentTypes()),
                hasItems("definition-1", "definition-2", "definition-3"));
    }
}
