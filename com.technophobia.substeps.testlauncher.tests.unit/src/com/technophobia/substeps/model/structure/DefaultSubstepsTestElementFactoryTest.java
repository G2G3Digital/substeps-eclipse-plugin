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

import com.technophobia.substeps.supplier.Supplier;

@RunWith(JMock.class)
public class DefaultSubstepsTestElementFactoryTest {

    private Mockery context;

    private SubstepsTestElementFactory factory;
    private Supplier<SubstepsTestParentElement> parentSupplier;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.factory = new DefaultSubstepsTestElementFactory();
        this.parentSupplier = context.mock(Supplier.class);
    }


    @Test
    public void canCreateParentElement() {
        final String testEntryString = "1,1: Some input,true,1";

        final SubstepsTestParentElement parent = context.mock(SubstepsTestParentElement.class);

        context.checking(new Expectations() {
            {
                oneOf(parentSupplier).get();
                will(returnValue(parent));

                oneOf(parent).addChild(with(any(SubstepsTestElement.class)));
            }
        });

        final SubstepsTestElement testElement = factory.createForTestEntryString(testEntryString, parentSupplier);
        assertThat(testElement, is(SubstepsTestParentElement.class));
        assertThat(testElement.getParent(), is(parent));
        assertThat(testElement.getId(), is("1"));
    }


    @Test
    public void canCreateLeafElement() {
        final String testEntryString = "2,2: Some input,false,1";

        final SubstepsTestParentElement parent = context.mock(SubstepsTestParentElement.class);

        context.checking(new Expectations() {
            {
                oneOf(parentSupplier).get();
                will(returnValue(parent));

                oneOf(parent).addChild(with(any(SubstepsTestElement.class)));
            }
        });

        final SubstepsTestElement testElement = factory.createForTestEntryString(testEntryString, parentSupplier);
        assertThat(testElement.getParent(), is(parent));
        assertThat(testElement, is(SubstepsTestLeafElement.class));
        assertThat(testElement.getId(), is("2"));
    }
}
