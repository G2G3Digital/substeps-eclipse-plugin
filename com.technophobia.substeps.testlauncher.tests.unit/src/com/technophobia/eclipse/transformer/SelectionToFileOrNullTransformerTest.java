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
