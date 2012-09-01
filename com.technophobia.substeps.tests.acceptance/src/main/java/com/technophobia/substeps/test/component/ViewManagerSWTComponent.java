package com.technophobia.substeps.test.component;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

import com.technophobia.substeps.test.steps.SWTBotInitialiser;

public class ViewManagerSWTComponent extends AbstractSWTLocatable<SWTWorkbenchBot> implements
        SWTRootComponent<SWTWorkbenchBot> {

    public ViewSWTComponent viewByTitle(final String title) {
        return new ViewSWTComponent(title, this);
    }


    @Override
    public SWTWorkbenchBot doLocate() {
        return SWTBotInitialiser.bot();
    }

}
