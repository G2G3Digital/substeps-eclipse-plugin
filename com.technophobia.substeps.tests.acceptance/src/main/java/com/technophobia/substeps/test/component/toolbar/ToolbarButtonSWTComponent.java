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
package com.technophobia.substeps.test.component.toolbar;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;

import com.technophobia.substeps.test.component.AbstractSWTLocatable;
import com.technophobia.substeps.test.component.SWTComponent;
import com.technophobia.substeps.test.component.SWTLocator;
import com.technophobia.substeps.test.component.SWTRootComponent;

public class ToolbarButtonSWTComponent extends AbstractSWTLocatable<SWTBotToolbarButton> implements
        SWTComponent<SWTBotToolbarButton> {

    private final SWTRootComponent<SWTBot> parent;
    private final SWTLocator<SWTBot, SWTBotToolbarButton> locator;


    public ToolbarButtonSWTComponent(final SWTLocator<SWTBot, SWTBotToolbarButton> locator,
            final SWTRootComponent<SWTBot> parent) {
        this.locator = locator;
        this.parent = parent;
    }


    public void click() {
        locate().click();
    }


    public boolean isPresent() {
        return locate() != null;
    }


    @Override
    public SWTBotToolbarButton doLocate() {
        return locator.locate(parent.locate());
    }
}
