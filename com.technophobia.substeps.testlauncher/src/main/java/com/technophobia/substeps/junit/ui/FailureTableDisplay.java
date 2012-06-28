package com.technophobia.substeps.junit.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.technophobia.substeps.table.LineType;

public class FailureTableDisplay implements TraceDisplay {
    private final Table table;

    private final Image exceptionIcon; //$NON-NLS-1$

    private final Image stackIcon; //$NON-NLS-1$


    public FailureTableDisplay(final Table table, final SubstepsIconProvider iconProvider) {
        this.table = table;
        exceptionIcon = iconProvider.imageFor(SubstepsIcon.Exception);
        stackIcon = iconProvider.imageFor(SubstepsIcon.Stack);
    }


    /* (non-Javadoc)
     * @see com.technophobia.substeps.junit.ui.TraceDisplay#addTraceLine(com.technophobia.substeps.table.LineType, java.lang.String)
     */
    @Override
    public void addTraceLine(final LineType lineType, final String label) {
        final TableItem tableItem = newTableItem();
        switch (lineType) {
        case EXCEPTION:
            tableItem.setImage(exceptionIcon);
            break;
        case STACK:
            tableItem.setImage(stackIcon);
            break;
        case NORMAL:
        default:
            break;
        }
        tableItem.setText(label);
    }


    public Image getExceptionIcon() {
        return exceptionIcon;
    }


    public Image getStackIcon() {
        return stackIcon;
    }


    public Table getTable() {
        return table;
    }


    public TableItem newTableItem() {
        return new TableItem(table, SWT.NONE);
    }
}
