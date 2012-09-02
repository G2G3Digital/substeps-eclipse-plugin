package com.technophobia.substeps.test.component;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

import com.technophobia.substeps.test.steps.SWTBotInitialiser;

public class EditorSWTComponent extends AbstractSWTLocatable<SWTBotEditor> implements
        SWTWorkbenchComponent<SWTBotEditor> {

    public void focus() {
        locate().setFocus();
    }


    public void setContentsTo(final String text) {
        locate().toTextEditor().setText(text);
    }


    public void save() {
        locate().save();
    }


    public void closeAll() {
        final SWTBotEditor editor = locate();
        editor.close();
    }


    public String content() {
        return locate().toTextEditor().getText();
    }


    @Override
    public SWTBotEditor doLocate() {
        return SWTBotInitialiser.bot().activeEditor();
    }
}
