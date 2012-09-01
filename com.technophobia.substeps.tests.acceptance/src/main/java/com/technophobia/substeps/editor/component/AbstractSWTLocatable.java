package com.technophobia.substeps.editor.component;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;

public abstract class AbstractSWTLocatable<T> implements SWTLocatable<T> {

	@Override
	public T locate() {
		final long targetTime = System.currentTimeMillis() + 5000;

		while (System.currentTimeMillis() < targetTime) {
			try {
				final T result = doLocate();
				if (result != null) {
					return result;
				}
			} catch (final WidgetNotFoundException ex) {
				// swallow it and try again, if we have time
			}
		}
		// still haven't found it - try one last time, in case the SWTBot
		// framework used all the time looking for it once

		return doLocate();
	}

	protected abstract T doLocate();
}
