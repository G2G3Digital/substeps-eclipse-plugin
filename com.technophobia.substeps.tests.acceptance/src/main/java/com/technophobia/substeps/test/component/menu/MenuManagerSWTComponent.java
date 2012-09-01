package com.technophobia.substeps.test.component.menu;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.technophobia.substeps.test.component.AbstractSWTLocatable;
import com.technophobia.substeps.test.component.SWTRootComponent;
import com.technophobia.substeps.test.steps.SWTBotInitialiser;

public class MenuManagerSWTComponent extends AbstractSWTLocatable<SWTBot> implements SWTRootComponent<SWTBot> {

    public MenuSWTComponent menuFor(final String title) {
        return new MenuSWTComponent(title, this);
    }


    @Override
    public SWTBot doLocate() {
        return SWTBotInitialiser.bot();
    }
}
