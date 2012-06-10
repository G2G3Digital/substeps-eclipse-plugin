package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;

public class TextFieldSWTComponent extends AbstractSWTLocatable<SWTBotText>
		implements SWTComponent<SWTBotText> {

	private final SWTLocatable<SWTBot> parent;
	private final String label;

	public TextFieldSWTComponent(final String label,
			final SWTLocatable<SWTBot> parent) {
		this.label = label;
		this.parent = parent;
	}

	public void setText(final String text) {
		locate().setText(text);
	}

	@Override
	public SWTBotText doLocate() {
		return parent.locate().textWithLabel(label);
	}
}
