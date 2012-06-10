package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

import com.technophobia.substeps.editor.steps.SWTBotInitialiser;

public class ViewManagerSWTComponent extends
		AbstractSWTLocatable<SWTWorkbenchBot> implements
		SWTRootComponent<SWTWorkbenchBot> {

	public ViewSWTComponent viewByTitle(final String title) {
		return new ViewSWTComponent(title, this);
	}

	@Override
	public SWTWorkbenchBot doLocate() {
		return SWTBotInitialiser.workbenchBot();
	}

}
