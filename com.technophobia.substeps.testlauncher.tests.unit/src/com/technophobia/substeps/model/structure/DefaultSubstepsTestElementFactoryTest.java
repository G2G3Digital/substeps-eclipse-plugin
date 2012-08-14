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
