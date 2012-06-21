package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.IJUnitHelpContextIds;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.technophobia.eclipse.ui.Notifier;

/**
 * Toggles console auto-scroll
 */
public class ScrollLockAction extends Action {

    private final Notifier<Boolean> autoScrollNotifier;


    public ScrollLockAction(final Notifier<Boolean> autoScrollNotifier) {
        super(JUnitMessages.ScrollLockAction_action_label);
        this.autoScrollNotifier = autoScrollNotifier;
        setToolTipText(JUnitMessages.ScrollLockAction_action_tooltip);
        setDisabledImageDescriptor(JUnitPlugin.getImageDescriptor("dlcl16/lock.gif")); //$NON-NLS-1$
        setHoverImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/lock.gif")); //$NON-NLS-1$
        setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/lock.gif")); //$NON-NLS-1$
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJUnitHelpContextIds.OUTPUT_SCROLL_LOCK_ACTION);
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
