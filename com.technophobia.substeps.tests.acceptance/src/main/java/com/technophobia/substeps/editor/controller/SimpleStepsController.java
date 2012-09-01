package com.technophobia.substeps.editor.controller;

import com.technophobia.substeps.editor.component.general.GeneralDialogSWTComponent;
import com.technophobia.substeps.editor.component.toolbar.ToolbarSWTComponent;

public class SimpleStepsController extends AbstractSWTController {

    public boolean isToolbarButtonPresent(final String mnemonicText) {
        final ToolbarSWTComponent toolbar = new ToolbarSWTComponent();

        return toolbar.buttonForMnemonic(mnemonicText).isPresent();
    }


    public void clickToolbarButton(final String mnemonicText) {
        final ToolbarSWTComponent toolbar = new ToolbarSWTComponent();
        toolbar.buttonForMnemonic(mnemonicText).click();
    }


    public boolean isDialogPresent(final String dialogTitle) {
        final GeneralDialogSWTComponent dialog = new GeneralDialogSWTComponent("Test");
        return dialog.isDialogPresent(dialogTitle);
    }


    public void clickButton(final String string) {
        final GeneralDialogSWTComponent dialog = new GeneralDialogSWTComponent("Test");
        dialog.clickButton("OK");
    }

}
