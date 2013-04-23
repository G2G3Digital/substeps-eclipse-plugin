package com.technophobia.substeps.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.supplier.Callback1;
import com.technophobia.substeps.supplier.Predicate;

@RunWith(JMock.class)
public class TemporaryModelEnhancerTest {

    private Mockery context;

    private Callback1<String> enhancement;
    private Callback1<String> detraction;
    private ModelOperation<String> operation;
    private Predicate<String> enhancementRequiredPredicate;

    private TemporaryModelEnhancer<String> modelEnhancer;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.enhancement = context.mock(Callback1.class, "enhancement");
        this.detraction = context.mock(Callback1.class, "detraction");
        this.operation = context.mock(ModelOperation.class, "operation");
        this.enhancementRequiredPredicate = context.mock(Predicate.class, "enhancementRequiredPredicate");

        this.modelEnhancer = new TemporaryModelEnhancer<String>(enhancement, detraction, operation,
                enhancementRequiredPredicate);
    }


    @Test
    public void noEnhancementDetractionAppliedIfNotRequired() throws Exception {

        final Sequence sequence = context.sequence("executionSequence");
        context.checking(new Expectations() {
            {
                oneOf(enhancementRequiredPredicate).forModel("test");
                will(returnValue(false));
                inSequence(sequence);

                oneOf(operation).doOperationOn("test");
                inSequence(sequence);
            }
        });

        modelEnhancer.doOperationFor("test");
    }


    @Test
    public void enhancementAndDetractionAppliedIfRequired() throws Exception {

        final Sequence sequence = context.sequence("executionSequence");
        context.checking(new Expectations() {
            {
                oneOf(enhancementRequiredPredicate).forModel("test");
                will(returnValue(true));
                inSequence(sequence);

                oneOf(enhancement).doCallback("test");
                inSequence(sequence);

                oneOf(operation).doOperationOn("test");
                inSequence(sequence);

                oneOf(detraction).doCallback("test");
                inSequence(sequence);
            }
        });

        modelEnhancer.doOperationFor("test");
    }


    @Test(expected = CoreException.class)
    public void detractionAppliedEvenIfExceptionOccurs() throws Exception {

        final Sequence sequence = context.sequence("executionSequence");
        context.checking(new Expectations() {
            {
                oneOf(enhancementRequiredPredicate).forModel("test");
                will(returnValue(true));
                inSequence(sequence);

                oneOf(enhancement).doCallback("test");
                inSequence(sequence);

                oneOf(operation).doOperationOn("test");
                will(throwException(new CoreException(new Status(IStatus.ERROR, "plugin", "Message"))));
                inSequence(sequence);

                oneOf(detraction).doCallback("test");
                inSequence(sequence);
            }
        });

        modelEnhancer.doOperationFor("test");
    }
}
