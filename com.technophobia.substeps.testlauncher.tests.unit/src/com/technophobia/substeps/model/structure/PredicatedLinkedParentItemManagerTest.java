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
