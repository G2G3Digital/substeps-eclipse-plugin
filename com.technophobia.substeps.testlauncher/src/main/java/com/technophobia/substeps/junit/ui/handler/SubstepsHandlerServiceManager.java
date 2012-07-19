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
