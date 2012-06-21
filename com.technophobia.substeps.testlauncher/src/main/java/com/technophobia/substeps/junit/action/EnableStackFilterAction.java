package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.JUnitPreferencesConstants;
import org.eclipse.jdt.internal.junit.ui.IJUnitHelpContextIds;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Refreshable;

public class EnableStackFilterAction extends Action {

    private final Refreshable view;


    public EnableStackFilterAction(final Refreshable view) {
        super(JUnitMessages.EnableStackFilterAction_action_label);
        setDescription(JUnitMessages.EnableStackFilterAction_action_description);
        setToolTipText(JUnitMessages.EnableStackFilterAction_action_tooltip);

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