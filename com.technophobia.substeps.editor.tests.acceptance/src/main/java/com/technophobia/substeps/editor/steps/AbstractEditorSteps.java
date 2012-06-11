package com.technophobia.substeps.editor.steps;

import com.technophobia.substeps.editor.controller.SWTController;
import com.technophobia.substeps.editor.controller.SWTControllerFactory;

public abstract class AbstractEditorSteps {

	protected <T extends SWTController> T createController(
			final Class<? extends T> controllerClass) {

		return SWTControllerFactory.createFor(controllerClass);
	}
}
