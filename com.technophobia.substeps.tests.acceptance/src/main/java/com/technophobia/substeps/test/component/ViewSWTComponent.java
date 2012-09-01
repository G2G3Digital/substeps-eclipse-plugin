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
