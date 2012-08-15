package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

import com.technophobia.substeps.editor.steps.SWTBotInitialiser;

public class EditorSWTComponent extends AbstractSWTLocatable<SWTBotEditor>
		implements SWTWorkbenchComponent<SWTBotEditor> {

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
		locate().close();
	}

	public String content() {
		return locate().toTextEditor().getText();
	}

	@Override
	public SWTBotEditor doLocate() {
		return SWTBotInitialiser.bot().activeEditor();
	}
}
