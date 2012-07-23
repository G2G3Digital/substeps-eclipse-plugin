package com.technophobia.substeps.document.content.assist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.document.content.ContentTypeDefinitionFactory;
import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Supplier;

@RunWith(JMock.class)
public class ProcessedContentAssistantFactoryTest {

    private Mockery context;

    private ContentAssistantFactory contentAssistantFactory;
    private ContentTypeDefinitionFactory contentTypeDefinitionFactory;

    private Supplier<IContentAssistProcessor> processorSupplier;
    private Callback1<IContentAssistant> decorator1;
    private Callback1<IContentAssistant> decorator2;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.processorSupplier = context.mock(Supplier.class);
        this.contentTypeDefinitionFactory = context.mock(ContentTypeDefinitionFactory.class);
        this.decorator1 = context.mock(Callback1.class, "decorator1");
        this.decorator2 = context.mock(Callback1.class, "decorator2");

        this.contentAssistantFactory = new ProcessedContentAssistantFactory(processorSupplier,
                contentTypeDefinitionFactory, decorator1, decorator2);
    }


    @Test
    public void canCreateContentAssistant() {

        final IContentAssistProcessor contentAssistantProcessor = context.mock(IContentAssistProcessor.class);

        context.checking(new Expectations() {
            {
                oneOf(processorSupplier).get();
                will(returnValue(contentAssistantProcessor));

                oneOf(contentTypeDefinitionFactory).contentTypeIds();
                will(returnValue(new String[] { "contentType" }));

                oneOf(decorator1).doCallback(with(any(IContentAssistant.class)));
                oneOf(decorator2).doCallback(with(any(IContentAssistant.class)));
            }
        });

        final IContentAssistant contentAssist = contentAssistantFactory.createContentAssist();
        assertThat(contentAssist.getContentAssistProcessor("contentType"), is(contentAssistantProcessor));
    }
}
