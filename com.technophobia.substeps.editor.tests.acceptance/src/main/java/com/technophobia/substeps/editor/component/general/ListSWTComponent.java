package com.technophobia.substeps.editor.component.general;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotList;

import com.technophobia.substeps.editor.component.AbstractSWTLocatable;
import com.technophobia.substeps.editor.component.SWTComponent;
import com.technophobia.substeps.editor.component.SWTLocatable;

public class ListSWTComponent extends AbstractSWTLocatable<SWTBotList>
		implements SWTComponent<SWTBotList> {

	private final SWTLocatable<SWTBot> parent;

	public ListSWTComponent(final SWTLocatable<SWTBot> parent) {
		this.parent = parent;
	}

	public void select(final String listItem) {
		locate().select(listItem);
	}

	@Override
	protected SWTBotList doLocate() {
		return parent.locate().list();
	}
}
