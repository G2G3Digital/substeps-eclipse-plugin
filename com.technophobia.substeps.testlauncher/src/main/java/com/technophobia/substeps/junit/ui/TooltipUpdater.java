package com.technophobia.substeps.junit.ui;

import java.text.MessageFormat;

import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.UiUpdater;

public class TooltipUpdater implements UiUpdater {

    private final Notifier<String> tooltipNotifier;


    public TooltipUpdater(final Notifier<String> tooltipNotifier) {
        this.tooltipNotifier = tooltipNotifier;
    }


    @Override
    public void doUpdate() {
        final String testKindDisplayStr = getTestKindDisplayName();

        if (testKindDisplayStr != null)
            tooltipNotifier.notify(MessageFormat.format(
                    SubstepsFeatureMessages.SubstepsFeatureTestRunnerViewPart_titleToolTip,
                    new Object[] { testKindDisplayStr }));
        else
            tooltipNotifier.notify("Test");
    }


    @Override
    public void reset() {
        tooltipNotifier.notify(null);
    }


    public String getTestKindDisplayName() {
        // final ITestKind kind = testRunSession.getTestRunnerKind();
        // if (!kind.isNull()) {
        // return kind.getDisplayName();
        // }
        return null;
    }
}
