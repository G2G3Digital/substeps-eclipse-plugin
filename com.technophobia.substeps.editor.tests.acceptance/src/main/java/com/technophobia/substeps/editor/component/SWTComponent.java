package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;

public interface SWTComponent<T extends AbstractSWTBot<?>> extends
		SWTLocatable<T> {
}