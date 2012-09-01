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
