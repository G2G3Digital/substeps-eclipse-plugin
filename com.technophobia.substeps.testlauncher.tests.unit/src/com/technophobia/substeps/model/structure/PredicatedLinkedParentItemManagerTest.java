package com.technophobia.substeps.model.structure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.transformer.Callback1;
import com.technophobia.substeps.supplier.Supplier;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class PredicatedLinkedParentItemManagerTest {

    private Mockery context;

    private Supplier<String> rootItemSupplier;
    private Callback1<String> nodeProcessor;
    private Transformer<String, Boolean> isCompletePredicate;

    private LinkedParentItemManager<String> parentItemManager;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.rootItemSupplier = context.mock(Supplier.class);
        this.nodeProcessor = context.mock(Callback1.class);
        this.isCompletePredicate = context.mock(Transformer.class);
        this.parentItemManager = new PredicatedLinkedParentItemManager<String>(rootItemSupplier, nodeProcessor,
                isCompletePredicate);
    }


    @Test
    public void getReturnsRootIfNoParentsAdded() {
        context.checking(new Expectations() {
            {
                oneOf(rootItemSupplier).get();
                will(returnValue("root"));
            }
        });

        assertThat(parentItemManager.get(), is("root"));
    }


    @Test
    public void getReturnsRootIfResetted() {

        context.checking(new Expectations() {
            {
                oneOf(rootItemSupplier).get();
                will(returnValue("root"));
            }
        });

        parentItemManager.addNode("item1");
        parentItemManager.addNode("item2");
        parentItemManager.reset();
        assertThat(parentItemManager.get(), is("root"));
    }


    @Test
    public void getReturnsLastItemAdded() {

        parentItemManager.addNode("item1");
        parentItemManager.addNode("item2");
        assertThat(parentItemManager.get(), is("item2"));
    }


    @Test
    public void completingAParentRemovesIt() {
        context.checking(new Expectations() {
            {

                oneOf(nodeProcessor).callback("item2");

                oneOf(isCompletePredicate).from("item2");
                will(returnValue(true));
            }
        });

        parentItemManager.addNode("item1");
        parentItemManager.addNode("item2");

        parentItemManager.processOutstandingChild();
        assertThat(parentItemManager.get(), is("item1"));
    }


    @Test
    public void notCompletingAParentMaintainsIt() {
        context.checking(new Expectations() {
            {

                oneOf(nodeProcessor).callback("item2");

                oneOf(isCompletePredicate).from("item2");
                will(returnValue(false));
            }
        });

        parentItemManager.addNode("item1");
        parentItemManager.addNode("item2");

        parentItemManager.processOutstandingChild();
        assertThat(parentItemManager.get(), is("item2"));
    }
}
