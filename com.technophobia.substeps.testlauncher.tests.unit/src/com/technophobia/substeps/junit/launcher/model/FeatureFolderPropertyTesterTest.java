package com.technophobia.substeps.junit.launcher.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFolder;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.supplier.Predicate;

@RunWith(JMock.class)
public class FeatureFolderPropertyTesterTest {

    private Mockery context;

    private Predicate<IFolder> predicate;

    private PropertyTester propertyTester;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.predicate = context.mock(Predicate.class);
        this.propertyTester = new FeatureFolderPropertyTester(predicate);
    }


    @Test
    public void returnsFalseIfNotTheCorrectProperty() {

        assertFalse(propertyTester.test(context.mock(IFolder.class), "different-property", null, null));
    }


    @Test
    public void returnsFalseIfPredicateReturnsFalse() {

        final IFolder folder = context.mock(IFolder.class);
        context.checking(new Expectations() {
            {
                oneOf(predicate).forModel(folder);
                will(returnValue(false));
            }
        });

        assertFalse(propertyTester.test(folder, "isFeatureFolder", null, null));
    }


    @Test
    public void returnsTrueIfPredicateReturnsTrue() {

        final IFolder folder = context.mock(IFolder.class);
        context.checking(new Expectations() {
            {
                oneOf(predicate).forModel(folder);
                will(returnValue(true));
            }
        });

        assertTrue(propertyTester.test(folder, "isFeatureFolder", null, null));
    }
}
