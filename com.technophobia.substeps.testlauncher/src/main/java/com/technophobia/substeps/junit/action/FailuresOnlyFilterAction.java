package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.SubstepsFailureTraceIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;

public class FailuresOnlyFilterAction extends Action {

    private final Notifier<Boolean> showFailuresNotifier;


    public FailuresOnlyFilterAction(final Notifier<Boolean> showFailuresNotifier,
            final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_show_failures_only, AS_CHECK_BOX);
        this.showFailuresNotifier = showFailuresNotifier;
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_show_failures_only);
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsFailureTraceIcon.Failures)); //$NON-NLS-1$
    }


    @Override
    public void run() {
        showFailuresNotifier.notify(Boolean.valueOf(isChecked()));
    }
}
