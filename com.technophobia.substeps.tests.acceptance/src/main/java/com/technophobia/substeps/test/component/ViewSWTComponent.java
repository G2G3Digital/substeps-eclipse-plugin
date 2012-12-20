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

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;

import com.technophobia.substeps.test.component.general.TreeSWTComponent;

public class ViewSWTComponent extends AbstractSWTLocatable<SWTBotView> implements SWTWorkbenchComponent<SWTBotView> {

    private final String viewTitle;
    private final SWTRootComponent<SWTWorkbenchBot> parent;


    public ViewSWTComponent(final String viewTitle, final SWTRootComponent<SWTWorkbenchBot> parent) {
        this.viewTitle = viewTitle;
        this.parent = parent;
    }


    public TreeSWTComponent treeInView() {
        return new TreeSWTComponent(this);
    }


    @Override
    public SWTBotView doLocate() {
        return parent.locate().viewByTitle(viewTitle);
    }
}
