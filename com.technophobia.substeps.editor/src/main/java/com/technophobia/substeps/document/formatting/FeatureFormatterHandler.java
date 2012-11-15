package com.technophobia.substeps.document.formatting;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.technophobia.eclipse.editor.FormattableEditorPart;
import com.technophobia.eclipse.log.Logger;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Transformer;

public class FeatureFormatterHandler extends AbstractHandler {

    private final Transformer<ExecutionEvent, IEditorPart> editorForEventSupplier;
    private final Logger logger;


    public FeatureFormatterHandler() {
        this(new ActiveEditorSupplier(), FeatureEditorPlugin.instance());
    }


    public FeatureFormatterHandler(final Transformer<ExecutionEvent, IEditorPart> editorForEventSupplier,
            final Logger logger) {
        this.editorForEventSupplier = editorForEventSupplier;
        this.logger = logger;
    }


    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {

        final IEditorPart editor = editorForEventSupplier.from(event);
        if (editor != null) {
            if (editor instanceof FormattableEditorPart) {
                ((FormattableEditorPart) editor).doFormat();
            } else {
                logger.log(IStatus.WARNING,
                        "Was expecting editor to be of type " + FormattableEditorPart.class.getName()
                                + ", but instead was " + editor.getClass().getName());
            }
        } else {
            logger.log(IStatus.WARNING, "Could not find a valid editor");
        }
        return null;
    }

    private static final class ActiveEditorSupplier implements Transformer<ExecutionEvent, IEditorPart> {

        @Override
        public IEditorPart from(final ExecutionEvent event) {
            final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
            return window.getActivePage().getActiveEditor();

        }

    }
}
