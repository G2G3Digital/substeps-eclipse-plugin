package com.technophobia.substeps.editor.component.general;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

import com.technophobia.substeps.editor.SWTTestUtil;
import com.technophobia.substeps.editor.component.AbstractSWTLocatable;
import com.technophobia.substeps.editor.component.SWTRootComponent;
import com.technophobia.substeps.editor.steps.SWTBotInitialiser;

public class GeneralDialogSWTComponent extends AbstractSWTLocatable<SWTBot> implements SWTRootComponent<SWTBot> {

    //
    // Commands
    //

    // probably needs moving - not specific to dialogs
    public void clickButton(final String buttonText) {
        locate().button(buttonText).click();
    }


    public void setFocus(final String dialogName) {
        final SWTBot bot = locate();
        try {
            Thread.sleep(3000);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final SWTBotShell shell = SWTTestUtil.dialogNamed(bot, dialogName);
        shell.activate();
        // SWTTestUtil.setActiveShellHack(shell.widget);
        shell.setFocus();
    }


    public TreeSWTComponent tree() {
        return new TreeSWTComponent(this);
    }


    public ListSWTComponent list() {
        return new ListSWTComponent(this);
    }


    public TableSWTComponent table() {
        return new TableSWTComponent(this);
    }


    //
    // Queries
    //
    public boolean isDialogPresent(final String dialogTitle) {
        try {
            locate().waitUntil(Conditions.shellIsActive(dialogTitle));
            return true;
        } catch (final TimeoutException ex) {
            return false;
        }
    }


    @Override
    public SWTBot doLocate() {
        return SWTBotInitialiser.bot();
    }
}
