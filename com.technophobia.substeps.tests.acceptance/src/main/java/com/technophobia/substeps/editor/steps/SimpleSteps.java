package com.technophobia.substeps.editor.steps;

import static org.junit.Assert.assertTrue;

import com.technophobia.substeps.editor.controller.SimpleStepsController;
import com.technophobia.substeps.model.SubSteps.Step;

public class SimpleSteps extends AbstractEditorSteps {

	@Step("When I press the \"([^\"]*)\" toolbar button")
	public void pressToolbarButton(final String mnemonicText) {
		final SimpleStepsController controller = createController(SimpleStepsController.class);

		assertTrue(controller.isToolbarButtonPresent(mnemonicText));
		controller.clickToolbarButton(mnemonicText);
	}

	@Step("Then the \"([^\"]*)\" dialog will be displayed")
	public void assertDialogVisible(final String dialogTitle) {
		final SimpleStepsController controller = createController(SimpleStepsController.class);

		assertTrue(controller.isDialogPresent(dialogTitle));
		controller.clickButton("OK");
	}
}
