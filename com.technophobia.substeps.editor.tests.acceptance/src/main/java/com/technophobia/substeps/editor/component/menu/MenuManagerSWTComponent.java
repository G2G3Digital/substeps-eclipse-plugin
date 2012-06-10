package com.technophobia.substeps.editor.component.menu;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.technophobia.substeps.editor.component.AbstractSWTLocatable;
import com.technophobia.substeps.editor.component.SWTRootComponent;
import com.technophobia.substeps.editor.steps.SWTBotInitialiser;

public class MenuManagerSWTComponent extends AbstractSWTLocatable<SWTBot>
		implements SWTRootComponent<SWTBot> {

	public MenuSWTComponent menuFor(final String title) {
		return new MenuSWTComponent(title, this);
	}

	@Override
	public SWTBot doLocate() {
		return SWTBotInitialiser.bot();
	}
}
