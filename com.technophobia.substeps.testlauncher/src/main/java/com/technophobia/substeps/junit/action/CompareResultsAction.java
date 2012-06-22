package com.technophobia.substeps.junit.action;

import org.eclipse.jdt.internal.junit.model.TestElement;
import org.eclipse.jdt.internal.junit.ui.CompareResultDialog;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.substeps.junit.ui.SubstepsFeatureMessages;

public class CompareResultsAction extends Action {

    private CompareResultDialog openDialog;
    private final Supplier<TestElement> failedTestSupplier;
    private final Shell shell;


    public CompareResultsAction(final Shell shell, final Supplier<TestElement> failedTestSupplier) {
        super(SubstepsFeatureMessages.CompareResultsAction_label);
        setDescription(SubstepsFeatureMessages.CompareResultsAction_description);
        setToolTipText(SubstepsFeatureMessages.CompareResultsAction_tooltip);

        setDisabledImageDescriptor(JUnitPlugin.getImageDescriptor("dlcl16/compare.gif")); //$NON-NLS-1$
        setHoverImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/compare.gif")); //$NON-NLS-1$
        setImageDescriptor(JUnitPlugin.getImageDescriptor("elcl16/compare.gif")); //$NON-NLS-1$
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
        final TestElement failedTest = failedTestSupplier.get();
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


    public void updateOpenDialog(final TestElement failedTest) {
        if (openDialog != null) {
            openDialog.setInput(failedTest);
        }
    }
}