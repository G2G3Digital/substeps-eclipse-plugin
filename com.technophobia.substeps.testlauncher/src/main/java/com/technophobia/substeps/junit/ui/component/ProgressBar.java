package com.technophobia.substeps.junit.ui.component;

public interface ProgressBar {

    void reset(boolean hasErrorsOrFailures, boolean equals, int ticksDone, int totalCount);

}
