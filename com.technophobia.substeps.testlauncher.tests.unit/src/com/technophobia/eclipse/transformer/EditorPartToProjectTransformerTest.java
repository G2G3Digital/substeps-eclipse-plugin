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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class EditorPartToProjectTransformerTest {

    private Mockery context;
    private Transformer<IEditorPart, IProject> transformer;


    @Before
    public void initialise() {
        this.context = new Mockery();

        this.transformer = new EditorPartToProjectTransformer();
    }


    @Test
    public void canTransform() {
        final IEditorPart editor = context.mock(IEditorPart.class);
        final IEditorInput editorInput = context.mock(IEditorInput.class);
        final IResource resource = context.mock(IResource.class);
        final IProject project = context.mock(IProject.class);

        context.checking(new Expectations() {
            {
                oneOf(editor).getEditorInput();
                will(returnValue(editorInput));

                oneOf(editorInput).getAdapter(IResource.class);
                will(returnValue(resource));

                oneOf(resource).getProject();
                will(returnValue(project));
            }
        });

        assertThat(transformer.from(editor), is(project));
    }
}
