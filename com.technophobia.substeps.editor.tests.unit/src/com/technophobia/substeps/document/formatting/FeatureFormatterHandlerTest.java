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
package com.technophobia.substeps.document.formatting;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.editor.FormattableEditorPart;
import com.technophobia.eclipse.log.PluginLogger;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class FeatureFormatterHandlerTest {

    private Mockery context;

    private Transformer<ExecutionEvent, IEditorPart> editorSupplier;
    private PluginLogger logger;

    private IHandler handler;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.editorSupplier = context.mock(Transformer.class);
        this.logger = context.mock(PluginLogger.class);

        this.handler = new FeatureFormatterHandler(editorSupplier, logger);

        // needed to prevent the log calls from throwing NullPointerExceptions
        new FeatureEditorPlugin();
    }


    @Test
    public void callsFormatOnEditor() throws Exception {
        final ExecutionEvent event = new ExecutionEvent();
        final FormattableEditorPart formattable = context.mock(FormattableEditorPart.class);

        context.checking(new Expectations() {
            {
                oneOf(editorSupplier).from(event);
                will(returnValue(formattable));

                oneOf(formattable).doFormat();
            }
        });
        handler.execute(event);
    }


    @Test
    public void doesNothingIfNotFormattable() throws Exception {
        final ExecutionEvent event = new ExecutionEvent();
        final IEditorPart editor = context.mock(IEditorPart.class);

        context.checking(new Expectations() {
            {
                oneOf(editorSupplier).from(event);
                will(returnValue(editor));

                allowing(logger).warn(with(any(String.class)));
            }
        });
        handler.execute(event);
    }


    @Test
    public void doesNothingIfNull() throws Exception {
        final ExecutionEvent event = new ExecutionEvent();

        context.checking(new Expectations() {
            {
                oneOf(editorSupplier).from(event);
                will(returnValue(null));

                allowing(logger).warn(with(any(String.class)));
            }
        });
        handler.execute(event);
    }
}
