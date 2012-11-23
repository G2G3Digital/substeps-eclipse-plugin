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
package com.technophobia.substeps.test.component.menu;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;

import com.technophobia.substeps.test.component.AbstractSWTLocatable;
import com.technophobia.substeps.test.component.SWTComponent;
import com.technophobia.substeps.test.component.SWTLocatable;

public class MenuSWTComponent extends AbstractSWTLocatable<SWTBotMenu> implements SWTComponent<SWTBotMenu> {

    private final String title;
    private final SWTLocatable<?> parent;


    public MenuSWTComponent(final String title, final SWTLocatable<?> parent) {
        this.title = title;
        this.parent = parent;
    }


    public MenuSWTComponent menuFor(final String newTitle) {
        return new MenuSWTComponent(newTitle, this);
    }


    public void click() {
        locate().click();
    }


    @Override
    public SWTBotMenu doLocate() {
        final Object locate = parent.locate();
        if (locate instanceof SWTBot) {
            return ((SWTBot) locate).menu(title);
        } else if (locate instanceof SWTBotMenu) {
            return ((SWTBotMenu) locate).menu(title);
        }
        throw new IllegalStateException("Could not locate menu from type " + locate.getClass().getName());
    }
}
