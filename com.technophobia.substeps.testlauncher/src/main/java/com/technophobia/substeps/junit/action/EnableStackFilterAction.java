package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.JUnitPreferencesConstants;
import org.eclipse.jdt.internal.junit.ui.IJUnitHelpContextIds;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Refreshable;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class EnableStackFilterAction extends Action {

    private final Refreshable view;


    public EnableStackFilterAction(final Refreshable view) {
        super(SubstepsFeatureMessages.EnableStackFilterAction_action_label);
        setDescription(SubstepsFeatureMessages.EnableStackFilterAction_action_description);
        setToolTipText(SubstepsFeatureMessages.EnableStackFilterAction_action_tooltip);

        setDisabledImageDescriptor(JUnitPlugin.getImageDescriptor("dlcl16/cfilter.gif")); //$NON-NLS-1$
        setHoverImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/cfilter.gif")); //$NON-NLS-1$
        setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/cfilter.gif")); //$NON-NLS-1$
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJUnitHelpContextIds.ENABLEFILTER_ACTION);

        this.view = view;
        setChecked(JUnitPreferencesConstants.getFilterStack());
    }


    /*
     * @see Action#actionPerformed
     */
    @Override
    public void run() {
        JUnitPreferencesConstants.setFilterStack(isChecked());
        view.refresh();
    }
}