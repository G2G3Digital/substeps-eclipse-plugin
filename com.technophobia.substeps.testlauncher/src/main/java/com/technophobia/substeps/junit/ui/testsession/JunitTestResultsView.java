package com.technophobia.substeps.junit.ui.testsession;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.junit.ui.TestRunnerViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class JunitTestResultsView implements TestResultsView {

    private final IWorkbenchWindow window;


    public JunitTestResultsView(final IWorkbenchWindow window) {
        this.window = window;
    }


    @Override
    public void showTestResultsView() {
        final IWorkbenchPage page = window.getActivePage();
        TestRunnerViewPart testRunner = null;

        if (page != null) {
            try { // show the result view
                testRunner = (TestRunnerViewPart) page.findView(TestRunnerViewPart.NAME);
                if (testRunner == null) {
                    final IWorkbenchPart activePart = page.getActivePart();
                    testRunner = (TestRunnerViewPart) page.showView(TestRunnerViewPart.NAME, null,
                            IWorkbenchPage.VIEW_VISIBLE);
                    // restore focus
                    page.activate(activePart);
                } else {
                    page.bringToTop(testRunner);
                }
            } catch (final PartInitException pie) {
                JUnitPlugin.log(pie);
            }
        }
    }

}
