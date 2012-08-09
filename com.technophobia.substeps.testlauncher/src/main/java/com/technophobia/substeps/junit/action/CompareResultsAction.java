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