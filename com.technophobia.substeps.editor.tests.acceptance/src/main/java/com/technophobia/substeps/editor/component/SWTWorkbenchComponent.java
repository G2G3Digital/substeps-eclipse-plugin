package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart;

public interface SWTWorkbenchComponent<T extends SWTBotWorkbenchPart<?>>
		extends SWTLocatable<T> {
}
