package com.technophobia.substeps.editor.steps;

import com.technophobia.substeps.editor.controller.FeatureFileEditorController;
import com.technophobia.substeps.editor.controller.SWTControllerFactory;
import com.technophobia.substeps.runner.JunitFeatureRunner.AfterEveryScenario;

public class EditorBeforeAndAfterExecutor {

	@AfterEveryScenario
	public void closeAllOpenEditors() {
		final FeatureFileEditorController controller = SWTControllerFactory
				.createFor(FeatureFileEditorController.class);

		controller.closeAllOpenEditors();
	}
}
