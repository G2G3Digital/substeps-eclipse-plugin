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
package com.technophobia.substeps.junit.ui.component;

import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.SubstepsTestIcon;

public class SubstepsCounterPanel extends Composite implements CounterPanel {

    protected Text numberOfErrors;
    protected Text numberOfFailures;
    protected Text numberOfRuns;
    protected int total;
    protected int ignoredCount;

    private final Image errorIcon;
    private final Image failureIcon;


    public SubstepsCounterPanel(final Composite parent, final SubstepsIconProvider iconProvider) {
        super(parent, SWT.WRAP);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 9;
        gridLayout.makeColumnsEqualWidth = false;
        gridLayout.marginWidth = 0;
        setLayout(gridLayout);

        this.errorIcon = iconProvider.imageFor(SubstepsTestIcon.SuiteError);
        this.failureIcon = iconProvider.imageFor(SubstepsTestIcon.SuiteFail);

        numberOfRuns = createLabel(SubstepsFeatureMessages.CounterPanel_label_runs, null, " 0/0  "); //$NON-NLS-1$
        numberOfErrors = createLabel(SubstepsFeatureMessages.CounterPanel_label_errors, errorIcon, " 0 "); //$NON-NLS-1$
        numberOfFailures = createLabel(SubstepsFeatureMessages.CounterPanel_label_failures, failureIcon, " 0 "); //$NON-NLS-1$
    }


    private Text createLabel(final String name, final Image image, final String init) {
        Label label = new Label(this, SWT.NONE);
        if (image != null) {
            image.setBackground(label.getBackground());
            label.setImage(image);
        }
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        label = new Label(this, SWT.NONE);
        label.setText(name);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        // label.setFont(JFaceResources.getBannerFont());

        final Text value = new Text(this, SWT.READ_ONLY);
        value.setText(init);
        // bug: 39661 Junit test counters do not repaint correctly [JUnit]
        value.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        return value;
    }


    public void reset() {
        setErrorValue(0);
        setFailureValue(0);
        setRunValue(0, 0);
        total = 0;
    }


    @Override
    public void setTotal(final int value) {
        total = value;
    }


    public int getTotal() {
        return total;
    }


    @Override
    public void setRunValue(final int value, final int ignoredCount) {
        String runString;
        if (ignoredCount == 0)
            runString = MessageFormat.format(SubstepsFeatureMessages.CounterPanel_runcount,
                    new Object[] { Integer.toString(value), Integer.toString(total) });
        else
            runString = MessageFormat.format(SubstepsFeatureMessages.CounterPanel_runcount_ignored, new Object[] {
                    Integer.toString(value), Integer.toString(total), Integer.toString(ignoredCount) });
        numberOfRuns.setText(runString);

        if (ignoredCount == 0 && ignoredCount > 0 || ignoredCount != 0 && ignoredCount == 0) {
            layout();
        } else {
            numberOfRuns.redraw();
            redraw();
        }
        this.ignoredCount = ignoredCount;
    }


    @Override
    public void setErrorValue(final int value) {
        numberOfErrors.setText(Integer.toString(value));
        redraw();
    }


    @Override
    public void setFailureValue(final int value) {
        numberOfFailures.setText(Integer.toString(value));
        redraw();
    }
}
