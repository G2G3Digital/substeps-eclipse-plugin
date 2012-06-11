package com.technophobia.substeps.editor.component.general;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;

import com.technophobia.substeps.editor.component.AbstractSWTLocatable;
import com.technophobia.substeps.editor.component.SWTComponent;
import com.technophobia.substeps.editor.component.SWTLocatable;

public class TableSWTComponent extends AbstractSWTLocatable<SWTBotTable>
		implements SWTComponent<SWTBotTable> {

	private final SWTLocatable<SWTBot> parent;

	public TableSWTComponent(final SWTLocatable<SWTBot> parent) {
		this.parent = parent;
	}

	public boolean hasItem(final String item) {
		return locate().containsItem(item);
	}

	public void select(final String item) {
		locate().getTableItem(item).select();
	}

	@Override
	protected SWTBotTable doLocate() {
		return parent.locate().table();
	}
}
