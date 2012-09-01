package com.technophobia.substeps.test.steps;

import com.technophobia.substeps.test.controller.SWTController;
import com.technophobia.substeps.test.controller.SWTControllerFactory;

public abstract class AbstractSteps {

    protected <T extends SWTController> T createController(final Class<? extends T> controllerClass) {

        return SWTControllerFactory.createFor(controllerClass);
    }
}
