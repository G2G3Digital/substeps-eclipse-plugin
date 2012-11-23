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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.technophobia.eclipse.editor.FormattableEditorPart;
import com.technophobia.eclipse.log.PluginLogger;
import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Transformer;

public class FeatureFormatterHandler extends AbstractHandler {

    private final Transformer<ExecutionEvent, IEditorPart> editorForEventSupplier;
    private final PluginLogger logger;


    public FeatureFormatterHandler() {
        this(new ActiveEditorSupplier(), FeatureEditorPlugin.instance());
    }


    public FeatureFormatterHandler(final Transformer<ExecutionEvent, IEditorPart> editorForEventSupplier,
            final PluginLogger logger) {
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
                logger.warn("Was expecting editor to be of type " + FormattableEditorPart.class.getName()
                        + ", but instead was " + editor.getClass().getName());
            }
        } else {
            logger.warn("Could not find a valid editor");
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
