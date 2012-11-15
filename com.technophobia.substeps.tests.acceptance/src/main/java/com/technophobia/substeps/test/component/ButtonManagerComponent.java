package com.technophobia.substeps.test.component;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.technophobia.substeps.test.steps.SWTBotInitialiser;

public class ButtonManagerComponent extends AbstractSWTLocatable<SWTBot> implements SWTRootComponent<SWTBot> {

    @Override
    public SWTBot doLocate() {
        return SWTBotInitialiser.bot();
    }


    public ButtonSWTComponent buttonFor(final String buttonLabel) {
        return new ButtonSWTComponent(buttonLabel, this);
    }

}
