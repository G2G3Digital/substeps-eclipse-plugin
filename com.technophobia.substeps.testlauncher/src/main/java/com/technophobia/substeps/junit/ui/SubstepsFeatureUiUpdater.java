package com.technophobia.substeps.junit.ui;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.eclipse.ui.render.NonDisposedUiUpdater;

public class SubstepsFeatureUiUpdater extends NonDisposedUiUpdater {

    private String infoMessage;
    private final UiUpdater infoMessageRenderer;
    private final UiUpdater testCounterRenderer;
    private final UiUpdater viewTitleRenderer;
    private final Supplier<TestRunStats> testRunStats;
    private final UiUpdater toolbarRenderer;
    private final FeatureViewer testViewer;


    public SubstepsFeatureUiUpdater(final Supplier<Boolean> disposedChecker, final UiUpdater infoMessageRenderer,
            final UiUpdater testCounterRenderer, final UiUpdater viewTitleRenderer, final UiUpdater toolbarRenderer,
            final FeatureViewer testViewer, final Supplier<TestRunStats> testRunStats) {
        super(disposedChecker);
        this.infoMessageRenderer = infoMessageRenderer;
        this.testCounterRenderer = testCounterRenderer;
        this.viewTitleRenderer = viewTitleRenderer;
        this.toolbarRenderer = toolbarRenderer;
        this.testViewer = testViewer;
        this.testRunStats = testRunStats;
    }


    @Override
    public void reset() {

    }


    public void registerInfoMessage(final String message) {
        this.infoMessage = message;
    }


    @Override
    protected void safeUpdate() {
        // TODO: hack - is this needed? Should really be covered by isDisposed()
        // if (sashForm.isDisposed())
        // return;

        infoMessageRenderer.doUpdate();
        testCounterRenderer.doUpdate();
        viewTitleRenderer.doUpdate();

        testViewer.processChangesInUI();
    }

}
