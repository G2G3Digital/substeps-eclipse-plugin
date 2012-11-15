package com.technophobia.substeps.test.editor.steps;

import com.technophobia.substeps.runner.setupteardown.Annotations.AfterEveryScenario;
import com.technophobia.substeps.test.controller.SWTControllerFactory;
import com.technophobia.substeps.test.editor.controller.FeatureFileEditorController;

public class EditorBeforeAndAfterExecutor {

    @AfterEveryScenario
    public void closeAllOpenEditors() {
        final FeatureFileEditorController controller = SWTControllerFactory
                .createFor(FeatureFileEditorController.class);

        controller.closeAllOpenEditors();
    }
}
