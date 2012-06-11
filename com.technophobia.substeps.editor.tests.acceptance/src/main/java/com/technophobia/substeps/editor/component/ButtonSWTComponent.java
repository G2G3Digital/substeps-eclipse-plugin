package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;

public class ButtonSWTComponent extends AbstractSWTLocatable<SWTBotButton>
		implements SWTComponent<SWTBotButton> {

	private final String buttonLabel;
	private final SWTLocatable<SWTBot> parent;

	public ButtonSWTComponent(final String buttonLabel,
			final SWTLocatable<SWTBot> parent) {
		this.buttonLabel = buttonLabel;
		this.parent = parent;
	}

	public void click() {
		locate().click();
	}

	@Override
	public SWTBotButton doLocate() {
		return parent.locate().button(buttonLabel);
	}
}
