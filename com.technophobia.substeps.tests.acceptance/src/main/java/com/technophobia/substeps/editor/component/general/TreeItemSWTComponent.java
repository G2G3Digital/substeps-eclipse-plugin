package com.technophobia.substeps.editor.component.general;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import com.technophobia.substeps.editor.component.AbstractSWTLocatable;
import com.technophobia.substeps.editor.component.SWTComponent;

public class TreeItemSWTComponent extends AbstractSWTLocatable<SWTBotTreeItem>
		implements SWTComponent<SWTBotTreeItem> {

	private final String nodeName;
	private final SWTComponent<? extends AbstractSWTBot<?>> parent;

	public TreeItemSWTComponent(final String nodeName,
			final TreeSWTComponent parent) {
		this.nodeName = nodeName;
		this.parent = parent;
	}

	protected TreeItemSWTComponent(final String nodeName,
			final TreeItemSWTComponent parent) {
		this.nodeName = nodeName;
		this.parent = parent;
	}

	public TreeItemSWTComponent select(final String nodeName) {
		locate().select(nodeName);
		return new TreeItemSWTComponent(nodeName, this);
	}

	public void expand() {
		locate().expand();
	}

	public void doubleClick() {
		locate().doubleClick();
	}

	public void clickDelete() {
		locate().contextMenu("Delete").click();
	}

	public boolean isItemExist(final String subItemName) {
		try {
			final SWTBotTreeItem subItem = locate().getNode(subItemName);
			return subItem != null;
		} catch (final WidgetNotFoundException ex) {
			return false;
		}
	}

	@Override
	public SWTBotTreeItem doLocate() {
		final AbstractSWTBot<?> locatedParent = parent.locate();
		if (locatedParent instanceof SWTBotTree) {
			return ((SWTBotTree) locatedParent).getTreeItem(nodeName);
		} else if (locatedParent instanceof SWTBotTreeItem) {
			return ((SWTBotTreeItem) locatedParent).getNode(nodeName);
		} else {
			throw new IllegalStateException("Expected a tree item, but got a "
					+ locatedParent.getClass());
		}
	}
}
