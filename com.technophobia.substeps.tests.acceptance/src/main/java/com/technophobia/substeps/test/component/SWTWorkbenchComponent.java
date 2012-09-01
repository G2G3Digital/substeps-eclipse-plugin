package com.technophobia.substeps.test.component;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart;

public interface SWTWorkbenchComponent<T extends SWTBotWorkbenchPart<?>> extends SWTLocatable<T> {
    // No-op
}
