package com.technophobia.substeps.editor;

import java.io.IOException;

import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.technophobia.substeps.runner.JunitFeatureRunner;

public class SubStepsSWTBotJunitClassRunner extends JunitFeatureRunner {

    // private static final Backend backend = new DeferredJavaBackend(null);

    public SubStepsSWTBotJunitClassRunner(final Class<?> clazz) throws InitializationError, IOException {
        super(clazz);
    }


    @Override
    public void run(final RunNotifier notifier) {
        try {
            Thread.sleep(10000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        final RunListener failureSpy = new ScreenshotCaptureListener();
        notifier.removeListener(failureSpy); // remove existing listeners that
                                             // could be added by suite or
                                             // class runners
        notifier.addListener(failureSpy);
        try {
            super.run(notifier);
        } finally {
            notifier.removeListener(failureSpy);
        }
    }
}
