package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.view.ViewLayout;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;

public class ShowTestHierarchyAction extends Action {

    private final Notifier<ViewLayout> layoutModeNotifier;


    public ShowTestHierarchyAction(final Notifier<ViewLayout> layoutModeNotifier,
            final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_hierarchical_layout, IAction.AS_CHECK_BOX);
        this.layoutModeNotifier = layoutModeNotifier;
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.HierarchicalLayout)); //$NON-NLS-1$
    }


    @Override
    public void run() {
        final ViewLayout mode = isChecked() ? ViewLayout.HIERARCHICAL : ViewLayout.FLAT;
        layoutModeNotifier.notify(mode);
    }
}