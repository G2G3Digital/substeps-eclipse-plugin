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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;

import com.technophobia.substeps.test.component.AbstractSWTLocatable;
import com.technophobia.substeps.test.component.SWTComponent;
import com.technophobia.substeps.test.component.SWTLocatable;

public class TableSWTComponent extends AbstractSWTLocatable<SWTBotTable> implements SWTComponent<SWTBotTable> {

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
