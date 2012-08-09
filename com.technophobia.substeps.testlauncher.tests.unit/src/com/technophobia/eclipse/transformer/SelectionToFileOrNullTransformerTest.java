package com.technophobia.eclipse.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class SelectionToFileOrNullTransformerTest {

    private Mockery context;

    private Transformer<ISelection, IFile> transformer;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.transformer = new SelectionToFileOrNullTransformer();
    }


    @Test
    public void returnsFileWhenValid() {
        final IStructuredSelection selection = context.mock(IStructuredSelection.class);

        final IFile file = context.mock(IFile.class);

        context.checking(new Expectations() {
            {
                oneOf(selection).size();
                will(returnValue(1));

                oneOf(selection).getFirstElement();
                will(returnValue(file));
            }
        });

        assertThat(transformer.from(selection), is(file));
    }


    @Test
    public void returnsNullWhenNotAStructuredSelection() {

        final ISelection selection = context.mock(ISelection.class);

        assertThat(transformer.from(selection), is(nullValue()));
    }


    @Test
    public void returnsNullWhenNotAFile() {
        final IStructuredSelection selection = context.mock(IStructuredSelection.class);
        final IResource resource = context.mock(IResource.class);

        context.checking(new Expectations() {
            {
                oneOf(selection).size();
                will(returnValue(1));

                oneOf(selection).getFirstElement();
                will(returnValue(resource));
            }
        });

        assertThat(transformer.from(selection), is(nullValue()));
    }


    @Test
    public void returnsNullWhenMoreThan1ItemSelected() {
        final IStructuredSelection selection = context.mock(IStructuredSelection.class);

        context.checking(new Expectations() {
            {
                oneOf(selection).size();
                will(returnValue(0));
            }
        });

        assertThat(transformer.from(selection), is(nullValue()));
    }
}
