package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.eclipse.ui.Notifier;

public class FailuresOnlyFilterAction extends Action {

    private final Notifier<Boolean> showFailuresNotifier;


    public FailuresOnlyFilterAction(final Notifier<Boolean> showFailuresNotifier) {
        super(JUnitMessages.TestRunnerViewPart_show_failures_only, AS_CHECK_BOX);
        this.showFailuresNotifier = showFailuresNotifier;
        setToolTipText(JUnitMessages.TestRunnerViewPart_show_failures_only);
        setImageDescriptor(JUnitPlugin.getImageDescriptor("obj16/failures.gif")); //$NON-NLS-1$
    }


    @Override
    public void run() {
        showFailuresNotifier.notify(isChecked());
    }
}
