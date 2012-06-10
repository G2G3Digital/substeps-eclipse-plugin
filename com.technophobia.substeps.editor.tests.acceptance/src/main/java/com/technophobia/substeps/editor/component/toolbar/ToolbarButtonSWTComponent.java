package com.technophobia.substeps.editor.component.toolbar;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;

import com.technophobia.substeps.editor.component.AbstractSWTLocatable;
import com.technophobia.substeps.editor.component.SWTComponent;
import com.technophobia.substeps.editor.component.SWTLocator;
import com.technophobia.substeps.editor.component.SWTRootComponent;

public class ToolbarButtonSWTComponent extends
		AbstractSWTLocatable<SWTBotToolbarButton> implements
		SWTComponent<SWTBotToolbarButton> {

	private final SWTRootComponent<SWTBot> parent;
	private final SWTLocator<SWTBot, SWTBotToolbarButton> locator;

	public ToolbarButtonSWTComponent(
			final SWTLocator<SWTBot, SWTBotToolbarButton> locator,
			final SWTRootComponent<SWTBot> parent) {
		this.locator = locator;
		this.parent = parent;
	}

	public void click() {
		locate().click();
	}

	public boolean isPresent() {
		return locate() != null;
	}

	@Override
	public SWTBotToolbarButton doLocate() {
		return locator.locate(parent.locate());
	}
}
