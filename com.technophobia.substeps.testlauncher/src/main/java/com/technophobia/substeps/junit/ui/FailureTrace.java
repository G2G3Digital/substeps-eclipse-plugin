package com.technophobia.substeps.junit.ui;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.IOpenEventListener;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;

import com.technophobia.eclipse.ui.Refreshable;
import com.technophobia.eclipse.ui.Resettable;
import com.technophobia.substeps.junit.action.CompareResultsAction;
import com.technophobia.substeps.junit.action.EnableStackFilterAction;
import com.technophobia.substeps.junit.action.OpenEditorAtLineAction;
import com.technophobia.substeps.junit.action.SubstepsCopyAction;
import com.technophobia.substeps.model.structure.SubstepsTestElement;
import com.technophobia.substeps.preferences.PreferencesConstants;
import com.technophobia.substeps.supplier.Supplier;

public class FailureTrace implements IMenuListener, Refreshable, Resettable {
    private static final int MAX_LABEL_LENGTH = 256;

    static final String FRAME_PREFIX = "at "; //$NON-NLS-1$
    private final Table table;
    private String inputTrace;
    private final Clipboard clipboard;
    private SubstepsTestElement failure;
    private final CompareResultsAction compareAction;
    private final FailureTableDisplay failureTableDisplay;

    private final Composite parent;


    public FailureTrace(final Composite parent, final Clipboard clipboard, final ToolBar toolBar,
            final SubstepsIconProvider iconProvider) {
        Assert.isNotNull(clipboard);

        // fill the failure trace viewer toolbar
        final ToolBarManager failureToolBarmanager = new ToolBarManager(toolBar);
        failureToolBarmanager.add(new EnableStackFilterAction(this, iconProvider));
        compareAction = new CompareResultsAction(parent.getShell(), failedTestSupplier(), iconProvider);
        compareAction.setEnabled(false);
        failureToolBarmanager.add(compareAction);
        failureToolBarmanager.update(true);

        this.table = new Table(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        this.clipboard = clipboard;
        this.parent = parent;

        final OpenStrategy handler = new OpenStrategy(table);
        handler.addOpenListener(new IOpenEventListener() {
            @Override
            public void handleOpen(final SelectionEvent e) {
                if (table.getSelectionIndex() == 0 && failure.isComparisonFailure()) {
                    compareAction.run();
                }
                if (table.getSelection().length != 0) {
                    final Action a = createOpenEditorAction(getSelectedText());
                    if (a != null)
                        a.run();
                }
            }
        });

        initMenu();

        failureTableDisplay = new FailureTableDisplay(table, iconProvider);
    }


    private void initMenu() {
        final MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(this);
        final Menu menu = menuMgr.createContextMenu(table);
        table.setMenu(menu);
    }


    @Override
    public void menuAboutToShow(final IMenuManager manager) {
        if (table.getSelectionCount() > 0) {
            final Action a = createOpenEditorAction(getSelectedText());
            if (a != null)
                manager.add(a);
            manager.add(new SubstepsCopyAction(parent.getShell(), this, clipboard));
        }
        // fix for bug 68058
        if (failure != null && failure.isComparisonFailure())
            manager.add(compareAction);
    }


    public String getTrace() {
        return inputTrace;
    }


    private String getSelectedText() {
        return table.getSelection()[0].getText();
    }


    private Action createOpenEditorAction(final String traceLine) {
        try {
            String testName = traceLine;
            testName = testName.substring(testName.indexOf(FRAME_PREFIX));
            testName = testName.substring(FRAME_PREFIX.length(), testName.lastIndexOf('(')).trim();
            testName = testName.substring(0, testName.lastIndexOf('.'));
            final int innerSeparatorIndex = testName.indexOf('$');
            if (innerSeparatorIndex != -1)
                testName = testName.substring(0, innerSeparatorIndex);

            String lineNumber = traceLine;
            lineNumber = lineNumber.substring(lineNumber.indexOf(':') + 1, lineNumber.lastIndexOf(')'));
            final int line = Integer.valueOf(lineNumber).intValue();
            return new OpenEditorAtLineAction(line);
        } catch (final NumberFormatException e) {
            // No-op
        } catch (final IndexOutOfBoundsException e) {
            // No-op
        }
        return null;
    }


    /**
     * Returns the composite used to present the trace
     * 
     * @return The composite
     */
    Composite getComposite() {
        return table;
    }


    /**
     * Refresh the table from the trace.
     */
    @Override
    public void refresh() {
        updateTable(inputTrace);
    }


    @Override
    public void reset() {
        clear();
    }


    /**
     * Shows a TestFailure
     * 
     * @param test
     *            the failed test
     */
    public void showFailure(final SubstepsTestElement test) {
        this.failure = test;
        String trace = ""; //$NON-NLS-1$
        updateEnablement(test);
        if (test != null)
            trace = test.getTrace();
        if (inputTrace == trace)
            return;
        inputTrace = trace;
        updateTable(trace);
    }


    public void updateEnablement(final SubstepsTestElement test) {
        final boolean enableCompare = test != null && test.isComparisonFailure();
        compareAction.setEnabled(enableCompare);
        if (enableCompare) {
            compareAction.updateOpenDialog(test);
        }
    }


    private void updateTable(final String trace) {
        if (trace == null || trace.trim().equals("")) { //$NON-NLS-1$
            clear();
            return;
        }
        table.setRedraw(false);
        table.removeAll();
        new TextualTrace(trace.trim(), getFilterPatterns()).display(failureTableDisplay, MAX_LABEL_LENGTH);
        table.setRedraw(true);
    }


    private String[] getFilterPatterns() {
        if (PreferencesConstants.getFilterStack())
            return PreferencesConstants.getFilterPatterns();
        return new String[0];
    }


    /**
     * Shows other information than a stack trace.
     * 
     * @param text
     *            the informational message to be shown
     */
    public void setInformation(final String text) {
        clear();
        final TableItem tableItem = failureTableDisplay.newTableItem();
        tableItem.setText(text);
    }


    /**
     * Clears the non-stack trace info
     */
    public void clear() {
        table.removeAll();
        inputTrace = null;
    }


    public Supplier<SubstepsTestElement> failedTestSupplier() {
        return new Supplier<SubstepsTestElement>() {
            @Override
            public SubstepsTestElement get() {
                return failure;
            }
        };
    }


    public Shell getShell() {
        return table.getShell();
    }


    public TraceDisplay getFailureTableDisplay() {
        return failureTableDisplay;
    }
}
