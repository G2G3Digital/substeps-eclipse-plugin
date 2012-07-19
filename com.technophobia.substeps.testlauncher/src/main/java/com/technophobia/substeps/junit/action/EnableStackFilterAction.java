package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Refreshable;
import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;
import com.technophobia.substeps.preferences.PreferencesConstants;

public class EnableStackFilterAction extends Action {

    private final Refreshable view;


    public EnableStackFilterAction(final Refreshable view, final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.EnableStackFilterAction_action_label);
        setDescription(SubstepsFeatureMessages.EnableStackFilterAction_action_description);
        setToolTipText(SubstepsFeatureMessages.EnableStackFilterAction_action_tooltip);

        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StackFilterDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StackFilterEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StackFilterEnabled)); //$NON-NLS-1$
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, SubstepsHelpContextIds.ENABLEFILTER_ACTION);

        this.view = view;
        setChecked(PreferencesConstants.getFilterStack());
    }


    /*
     * @see Action#actionPerformed
     */
    @Override
    public void run() {
        PreferencesConstants.setFilterStack(isChecked());
        view.refresh();
    }
}