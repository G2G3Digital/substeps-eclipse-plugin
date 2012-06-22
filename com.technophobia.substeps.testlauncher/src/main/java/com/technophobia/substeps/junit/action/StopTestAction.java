package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class StopTestAction extends Action {
    private final Supplier<TestRunSession> testRunSessionSupplier;
    private final Notifier<String> infoMessageUpdater;


    public StopTestAction(final Supplier<TestRunSession> testRunSessionSupplier,
            final Notifier<String> infoMessageUpdater) {
        this.testRunSessionSupplier = testRunSessionSupplier;
        this.infoMessageUpdater = infoMessageUpdater;
        setText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_stopaction_text);
        setToolTipText(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_stopaction_text);
        JUnitPlugin.setLocalImageDescriptors(this, "stop.gif"); //$NON-NLS-1$
    }


    @Override
    public void run() {
        stopTest();
        setEnabled(false);
    }


    private void stopTest() {
        final TestRunSession testRunSession = testRunSessionSupplier.get();
        if (testRunSession != null) {
            if (testRunSession.isRunning()) {
                infoMessageUpdater.notify(SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_message_stopping);
            }
            testRunSession.stopTestRun();
        }
    }
}
