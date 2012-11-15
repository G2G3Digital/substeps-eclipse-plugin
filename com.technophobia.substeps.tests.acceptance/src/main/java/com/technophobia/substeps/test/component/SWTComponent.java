package com.technophobia.substeps.test.component;

import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;

public interface SWTComponent<T extends AbstractSWTBot<?>> extends SWTLocatable<T> {

    // No-op
}