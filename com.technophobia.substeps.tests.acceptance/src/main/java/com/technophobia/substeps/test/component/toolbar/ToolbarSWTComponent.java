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
import com.technophobia.substeps.test.component.SWTLocator;
import com.technophobia.substeps.test.component.SWTRootComponent;
import com.technophobia.substeps.test.steps.SWTBotInitialiser;

public class ToolbarSWTComponent extends AbstractSWTLocatable<SWTBot> implements SWTRootComponent<SWTBot> {

    public ToolbarSWTComponent() {
    }


    //
    // Commands
    //

    public ToolbarButtonSWTComponent buttonForMnemonic(final String mnemonicText) {
        return new ToolbarButtonSWTComponent(findButtonWithMnemonic(mnemonicText), this);
    }


    public ToolbarButtonSWTComponent buttonForTooltip(final String tooltipText) {
        return new ToolbarButtonSWTComponent(findButtonWithTooltipText(tooltipText), this);
    }


    @Override
    public SWTBot doLocate() {
        return SWTBotInitialiser.bot();
    }


    private SWTLocator<SWTBot, SWTBotToolbarButton> findButtonWithMnemonic(final String mnemonicText) {
        return new SWTLocator<SWTBot, SWTBotToolbarButton>() {
            @Override
            public SWTBotToolbarButton locate(final SWTBot parent) {
                return parent.toolbarButtonWithTooltip(mnemonicText);
            }
        };
    }


    private SWTLocator<SWTBot, SWTBotToolbarButton> findButtonWithTooltipText(final String tooltipText) {
        return new SWTLocator<SWTBot, SWTBotToolbarButton>() {
            @Override
            public SWTBotToolbarButton locate(final SWTBot parent) {
                return parent.toolbarDropDownButtonWithTooltip(tooltipText);
            }
        };
    }
}
