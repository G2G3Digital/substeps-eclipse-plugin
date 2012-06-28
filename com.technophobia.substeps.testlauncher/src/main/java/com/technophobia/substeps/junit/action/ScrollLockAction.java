package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIcon;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.help.SubstepsHelpContextIds;

/**
 * Toggles console auto-scroll
 */
public class ScrollLockAction extends Action {

    private final Notifier<Boolean> autoScrollNotifier;


    public ScrollLockAction(final Notifier<Boolean> autoScrollNotifier, final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.ScrollLockAction_action_label);
        this.autoScrollNotifier = autoScrollNotifier;
        setToolTipText(SubstepsFeatureMessages.ScrollLockAction_action_tooltip);
        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.ScrollLockDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.ScrollLockEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsIcon.ScrollLockEnabled)); //$NON-NLS-1$
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, SubstepsHelpContextIds.OUTPUT_SCROLL_LOCK_ACTION);
        setChecked(false);
    }


    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
    public void run() {
        autoScrollNotifier.notify(!isChecked());
    }
}
