package com.technophobia.substeps.editor.steps;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;

import com.technophobia.substeps.model.SubSteps.Step;

public class BasicEditorSteps {

	@Step("Given the \"([^\"]*)\" view is not visible")
	public void ensureViewIsHidden(final String view) {
		final SWTWorkbenchBot workbenchBot = new SWTWorkbenchBot();
		try {
			final SWTBotView activeView = workbenchBot.activeView();
			if (view.equals(activeView.getTitle())) {
				workbenchBot.viewByTitle(view).close();
			}
		} catch (final WidgetNotFoundException ex) {
			// no view is active, no need to do anything
		}
	}
}
