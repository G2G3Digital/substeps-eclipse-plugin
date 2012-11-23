/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.test.component;

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
