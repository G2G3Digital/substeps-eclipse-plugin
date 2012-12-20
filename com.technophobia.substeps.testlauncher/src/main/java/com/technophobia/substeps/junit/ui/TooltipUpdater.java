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
