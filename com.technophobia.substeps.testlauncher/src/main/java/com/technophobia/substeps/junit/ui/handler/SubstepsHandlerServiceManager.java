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
package com.technophobia.substeps.junit.ui.handler;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;

public class SubstepsHandlerServiceManager {

    private final IHandlerService handlerService;

    private final Collection<IHandlerActivation> handlers;


    public SubstepsHandlerServiceManager(final IHandlerService handlerService) {
        this.handlerService = handlerService;
        this.handlers = new ArrayList<IHandlerActivation>();
    }


    public void activateHandlers(final String name, final IHandler handler) {
        handlers.add(handlerService.activateHandler(name, handler));
    }


    public void deactivateHandlers() {
        for (final IHandlerActivation handler : handlers) {
            handlerService.deactivateHandler(handler);
        }
    }
}
