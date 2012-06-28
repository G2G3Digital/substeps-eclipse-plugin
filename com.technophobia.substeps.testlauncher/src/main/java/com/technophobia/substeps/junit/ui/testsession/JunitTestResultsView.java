package com.technophobia.substeps.junit.ui.testsession;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.technophobia.substeps.FeatureRunnerPlugin;
import com.technophobia.substeps.junit.ui.SubstepsFeatureTestRunnerViewPart;

public class JunitTestResultsView implements TestResultsView {

    private final IWorkbenchWindow window;


    public JunitTestResultsView(final IWorkbenchWindow window) {
        this.window = window;
    }


    @Override
    public void showTestResultsView() {
        final IWorkbenchPage page = window.getActivePage();
        SubstepsFeatureTestRunnerViewPart testRunner = null;

        if (page != null) {
            try { // show the result view
                testRunner = (SubstepsFeatureTestRunnerViewPart) page.findView(SubstepsFeatureTestRunnerViewPart.NAME);
                if (testRunner == null) {
                    final IWorkbenchPart activePart = page.getActivePart();
                    testRunner = (SubstepsFeatureTestRunnerViewPart) page.showView(
                            SubstepsFeatureTestRunnerViewPart.NAME, null, IWorkbenchPage.VIEW_VISIBLE);
                    // restore focus
                    page.activate(activePart);
                } else {
                    page.bringToTop(testRunner);
                }
            } catch (final PartInitException pie) {
                FeatureRunnerPlugin.log(Status.ERROR, pie.getMessage());
            }
        }
    }

}
