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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;

public class TextFieldSWTComponent extends AbstractSWTLocatable<SWTBotText>
		implements SWTComponent<SWTBotText> {

	private final SWTLocatable<SWTBot> parent;
	private final String label;

	public TextFieldSWTComponent(final String label,
			final SWTLocatable<SWTBot> parent) {
		this.label = label;
		this.parent = parent;
	}

	public void setText(final String text) {
		locate().setText(text);
	}

	@Override
	public SWTBotText doLocate() {
		return parent.locate().textWithLabel(label);
	}
}
