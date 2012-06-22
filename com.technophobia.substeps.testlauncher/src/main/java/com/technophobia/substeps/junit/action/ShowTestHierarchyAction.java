package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.view.ViewLayout;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class ShowTestHierarchyAction extends Action {

    private Notifier<ViewLayout> layoutModeNotifier;


    public ShowTestHierarchyAction(final Notifier<ViewLayout> layoutModeNotifier) {
        this.layoutModeNotifier = layoutModeNotifier;
    }


    public ShowTestHierarchyAction() {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_hierarchical_layout, IAction.AS_CHECK_BOX);
        setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/hierarchicalLayout.gif")); //$NON-NLS-1$
    }


    @Override
    public void run() {
        final ViewLayout mode = isChecked() ? ViewLayout.HIERARCHICAL : ViewLayout.FLAT;
        layoutModeNotifier.notify(mode);
    }
}