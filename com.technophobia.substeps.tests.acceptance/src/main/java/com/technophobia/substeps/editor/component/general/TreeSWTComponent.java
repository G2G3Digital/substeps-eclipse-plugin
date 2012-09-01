package com.technophobia.substeps.editor.component.general;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import com.technophobia.substeps.editor.component.AbstractSWTLocatable;
import com.technophobia.substeps.editor.component.SWTComponent;
import com.technophobia.substeps.editor.component.SWTLocatable;

public class TreeSWTComponent extends AbstractSWTLocatable<SWTBotTree>
		implements SWTComponent<SWTBotTree> {

	final String nodeName;
	private final SWTLocatable<SWTBotTree> parent;

	public TreeSWTComponent(final SWTLocatable<?> parent) {
		this(null, new RootTreeSWTComponent(parent));
	}

	public TreeSWTComponent(final String nodeName,
			final SWTLocatable<SWTBotTree> parent) {
		this.nodeName = nodeName;
		this.parent = parent;
	}

	//
	// Commands
	//

	public TreeSWTComponent select(final String nodeName) {
		locate().select(nodeName);
		return new TreeSWTComponent(nodeName, this);
	}

	public TreeItemSWTComponent expandNode(final String nodeName) {
		locate().expandNode(nodeName);
		return new TreeItemSWTComponent(nodeName, this);
	}

	public void clickContextMenuItem(final String... menuItemHierarchy) {
		ContextMenuHelper.clickContextMenu(locate(), menuItemHierarchy);
	}

	//
	// Queries
	//

	public boolean isItemExist(final String projectName) {
		final SWTBotTreeItem[] items = locate().getAllItems();
		for (final SWTBotTreeItem item : items) {
			if (projectName.equals(item.getText())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public SWTBotTree doLocate() {
		if (nodeName != null) {
			return parent.locate().select(nodeName);
		}
		return parent.locate();
	}

	private static final class RootTreeSWTComponent extends
			AbstractSWTLocatable<SWTBotTree> implements
			SWTComponent<SWTBotTree> {
		private final SWTLocatable<?> parent;

		public RootTreeSWTComponent(final SWTLocatable<?> parent) {
			this.parent = parent;
		}

		@Override
		public SWTBotTree doLocate() {
			final Object locate = parent.locate();
			if (locate instanceof SWTBotView) {
				return ((SWTBotView) locate).bot().tree();
			} else if (locate instanceof SWTBot) {
				return ((SWTBot) locate).tree();
			}
			throw new IllegalStateException("Unexpected parent type "
					+ locate.getClass().getName());
		}
	}
}
