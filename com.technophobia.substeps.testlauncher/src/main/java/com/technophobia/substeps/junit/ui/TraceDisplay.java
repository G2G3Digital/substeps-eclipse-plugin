package com.technophobia.substeps.junit.ui;

import com.technophobia.substeps.table.LineType;

public interface TraceDisplay {

    public abstract void addTraceLine(final LineType lineType, final String label);

}