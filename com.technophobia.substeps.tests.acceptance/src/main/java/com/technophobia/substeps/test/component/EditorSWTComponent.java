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
