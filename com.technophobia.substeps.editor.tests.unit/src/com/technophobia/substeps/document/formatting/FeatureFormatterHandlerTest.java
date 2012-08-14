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
import com.technophobia.eclipse.log.Logger;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Transformer;

@RunWith(JMock.class)
public class FeatureFormatterHandlerTest {

    private Mockery context;

    private Transformer<ExecutionEvent, IEditorPart> editorSupplier;
    private Logger logger;

    private IHandler handler;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.editorSupplier = context.mock(Transformer.class);
        this.logger = context.mock(Logger.class);

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

                allowing(logger).log(with(any(int.class)), with(any(String.class)));
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

                allowing(logger).log(with(any(int.class)), with(any(String.class)));
            }
        });
        handler.execute(event);
    }
}
