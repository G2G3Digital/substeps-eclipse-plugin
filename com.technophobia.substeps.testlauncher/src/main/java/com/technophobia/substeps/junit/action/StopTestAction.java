package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.SubstepsRunSession;
import com.technophobia.substeps.supplier.Supplier;

public class StopTestAction extends Action {
    private final Supplier<SubstepsRunSession> substepsRunSessionSupplier;
    private final Notifier<String> infoMessageUpdater;


    public StopTestAction(final Supplier<SubstepsRunSession> substepsRunSessionSupplier,
            final Notifier<String> infoMessageUpdater, final SubstepsIconProvider iconProvider) {
        this.substepsRunSessionSupplier = substepsRunSessionSupplier;
        this.infoMessageUpdater = infoMessageUpdater;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_stopaction_text);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_stopaction_text);
        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StopDisbled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StopEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.StopEnabled)); //$NON-NLS-1$
    }


    @Override
    public void run() {
        stopTest();
        setEnabled(false);
    }


    private void stopTest() {
        final SubstepsRunSession substepsRunSession = substepsRunSessionSupplier.get();
        if (substepsRunSession != null) {
            if (substepsRunSession.isRunning()) {
                infoMessageUpdater.notify(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_stopping);
            }
            substepsRunSession.stopTestRun();
        }
    }
}
