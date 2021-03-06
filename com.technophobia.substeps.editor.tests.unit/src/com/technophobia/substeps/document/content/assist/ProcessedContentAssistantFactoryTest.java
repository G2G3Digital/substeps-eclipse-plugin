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
package com.technophobia.substeps.document.content.assist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.document.content.feature.definition.StepContentTypeDefinition;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Supplier;

@RunWith(JMock.class)
public class ProcessedContentAssistantFactoryTest {

    private Mockery context;

    private ContentAssistantFactory contentAssistantFactory;
    private Supplier<IContentAssistProcessor> processorSupplier;
    private Callback1<IContentAssistant> decorator1;
    private Callback1<IContentAssistant> decorator2;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.processorSupplier = context.mock(Supplier.class);
        this.decorator1 = context.mock(Callback1.class, "decorator1");
        this.decorator2 = context.mock(Callback1.class, "decorator2");

        this.contentAssistantFactory = new ProcessedContentAssistantFactory(processorSupplier, decorator1, decorator2);
    }


    @Test
    @Ignore("This is failing with a java.lang.SecurityException, class 'org.eclipse.jfact.text.BadLocationException's signer information does not match signer information of other classes in the same package - no idea why")
    public void canCreateContentAssistant() {

        final IContentAssistProcessor contentAssistantProcessor = context.mock(IContentAssistProcessor.class);

        context.checking(new Expectations() {
            {
                oneOf(processorSupplier).get();
                will(returnValue(contentAssistantProcessor));

                oneOf(decorator1).doCallback(with(any(IContentAssistant.class)));
                oneOf(decorator2).doCallback(with(any(IContentAssistant.class)));
            }
        });

        final IContentAssistant contentAssist = contentAssistantFactory.createContentAssist();
        assertThat(contentAssist.getContentAssistProcessor(StepContentTypeDefinition.CONTENT_TYPE_ID),
                is(contentAssistantProcessor));
    }
}
