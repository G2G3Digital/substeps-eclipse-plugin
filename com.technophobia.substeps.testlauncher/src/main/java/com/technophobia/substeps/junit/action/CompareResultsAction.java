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
package com.technophobia.substeps.junit.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.substeps.junit.ui.SubstepsControlsIcon;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;
import com.technophobia.substeps.junit.ui.SubstepsIconProvider;
import com.technophobia.substeps.junit.ui.dialog.CompareResultDialog;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.supplier.Supplier;

public class CompareResultsAction extends Action {

    private CompareResultDialog openDialog;
    private final Supplier<SubstepsTestElement> failedTestSupplier;
    private final Shell shell;


    public CompareResultsAction(final Shell shell, final Supplier<SubstepsTestElement> failedTestSupplier,
            final SubstepsIconProvider iconProvider) {
        super(SubstepsFeatureMessages.CompareResultsAction_label);
        setDescription(SubstepsFeatureMessages.CompareResultsAction_description);
        setToolTipText(SubstepsFeatureMessages.CompareResultsAction_tooltip);

        setDisabledImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.CompareDisabled)); //$NON-NLS-1$
        setHoverImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.CompareEnabled)); //$NON-NLS-1$
        setImageDescriptor(iconProvider.imageDescriptorFor(SubstepsControlsIcon.CompareEnabled)); //$NON-NLS-1$
        // PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
        // IJUnitHelpContextIds.ENABLEFILTER_ACTION);

        this.shell = shell;
        this.failedTestSupplier = failedTestSupplier;
    }


    /*
     * @see Action#actionPerformed
     */
    @Override
    public void run() {
        final SubstepsTestElement failedTest = failedTestSupplier.get();
        if (openDialog != null) {
            openDialog.setInput(failedTest);
            openDialog.getShell().setActive();

        } else {
            openDialog = new CompareResultDialog(shell, failedTest);
            openDialog.create();
            openDialog.getShell().addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(final DisposeEvent e) {
                    openDialog = null;
                }
            });
            openDialog.setBlockOnOpen(false);
            openDialog.open();
        }
    }


    public void updateOpenDialog(final SubstepsTestElement failedTest) {
        if (openDialog != null) {
            openDialog.setInput(failedTest);
        }
    }
}
