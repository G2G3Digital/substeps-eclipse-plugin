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

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import com.technophobia.substeps.test.component.AbstractSWTLocatable;
import com.technophobia.substeps.test.component.SWTComponent;

public class TreeItemSWTComponent extends AbstractSWTLocatable<SWTBotTreeItem> implements SWTComponent<SWTBotTreeItem> {

    private final String nodeName;
    private final SWTComponent<? extends AbstractSWTBot<?>> parent;


    public TreeItemSWTComponent(final String nodeName, final TreeSWTComponent parent) {
        this.nodeName = nodeName;
        this.parent = parent;
    }


    protected TreeItemSWTComponent(final String nodeName, final TreeItemSWTComponent parent) {
        this.nodeName = nodeName;
        this.parent = parent;
    }


    public TreeItemSWTComponent select(final String newNodeName) {
        locate().select(newNodeName);
        return new TreeItemSWTComponent(newNodeName, this);
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
            throw new IllegalStateException("Expected a tree item, but got a " + locatedParent.getClass());
        }
    }
}
