/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.test.runner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

import com.technophobia.substeps.runner.JunitFeatureRunner;

public class SubStepsSWTBotJunitClassRunner extends JunitFeatureRunner {

    // private static final Backend backend = new DeferredJavaBackend(null);

    public SubStepsSWTBotJunitClassRunner(final Class<?> clazz) {
        super(clazz);
    }


    @Override
    public void run(final RunNotifier notifier) {
        try {
            Thread.sleep(10000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        initialiseLogger();

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


    private void initialiseLogger() {
        try {
            final InputStream inputStream = new FileInputStream("src/main/resources/log4j.properties");
            final Properties props = new Properties();
            props.load(inputStream);
            PropertyConfigurator.configure(props);
        } catch (final IOException e) {
            throw new IllegalStateException("Could not initialise logger");
        }
    }
}
