package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.technophobia.substeps.editor.steps.SWTBotInitialiser;

public class FormEditorSWTComponent extends AbstractSWTLocatable<SWTBot>
		implements SWTRootComponent<SWTBot> {

	public TextFieldSWTComponent textWithLabel(final String label) {
		return new TextFieldSWTComponent(label, this);
	}

	@Override
	public SWTBot doLocate() {
		return SWTBotInitialiser.bot();
	}
}
