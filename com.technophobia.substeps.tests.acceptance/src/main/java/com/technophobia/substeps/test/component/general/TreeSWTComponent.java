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
package com.technophobia.substeps.test.component.general;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import com.technophobia.substeps.test.component.AbstractSWTLocatable;
import com.technophobia.substeps.test.component.SWTComponent;
import com.technophobia.substeps.test.component.SWTLocatable;

public class TreeSWTComponent extends AbstractSWTLocatable<SWTBotTree> implements SWTComponent<SWTBotTree> {

    final String nodeName;
    private final SWTLocatable<SWTBotTree> parent;


    public TreeSWTComponent(final SWTLocatable<?> parent) {
        this(null, new RootTreeSWTComponent(parent));
    }


    public TreeSWTComponent(final String nodeName, final SWTLocatable<SWTBotTree> parent) {
        this.nodeName = nodeName;
        this.parent = parent;
    }


    //
    // Commands
    //

    public TreeSWTComponent select(final String newNodeName) {
        locate().select(newNodeName);
        return new TreeSWTComponent(newNodeName, this);
    }


    public TreeItemSWTComponent expandNode(final String newNodeName) {
        locate().expandNode(newNodeName);
        return new TreeItemSWTComponent(newNodeName, this);
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

    private static final class RootTreeSWTComponent extends AbstractSWTLocatable<SWTBotTree> implements
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
            throw new IllegalStateException("Unexpected parent type " + locate.getClass().getName());
        }
    }
}
