package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class FailuresOnlyFilterAction extends Action {

    private final Notifier<Boolean> showFailuresNotifier;


    public FailuresOnlyFilterAction(final Notifier<Boolean> showFailuresNotifier) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_show_failures_only, AS_CHECK_BOX);
        this.showFailuresNotifier = showFailuresNotifier;
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_show_failures_only);
        setImageDescriptor(JUnitPlugin.getImageDescriptor("obj16/failures.gif")); //$NON-NLS-1$
    }


    @Override
    public void run() {
        showFailuresNotifier.notify(isChecked());
    }
}
