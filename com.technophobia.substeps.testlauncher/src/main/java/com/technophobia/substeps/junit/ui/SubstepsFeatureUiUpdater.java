package com.technophobia.substeps.junit.ui;

import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.eclipse.ui.render.NonDisposedUiUpdater;
import com.technophobia.substeps.junit.ui.component.FeatureViewer;
import com.technophobia.substeps.supplier.Supplier;

public class SubstepsFeatureUiUpdater extends NonDisposedUiUpdater {

    private final UiUpdater infoMessageRenderer;
    private final UiUpdater testCounterRenderer;
    private final UiUpdater viewTitleRenderer;
    private final Supplier<TestRunStats> testRunStats;
    private final FeatureViewer testViewer;
    private final SubstepsActionManager actionManager;


    public SubstepsFeatureUiUpdater(final Supplier<Boolean> disposedChecker, final UiUpdater infoMessageRenderer,
            final UiUpdater testCounterRenderer, final UiUpdater viewTitleRenderer, final FeatureViewer testViewer,
            final Supplier<TestRunStats> testRunStats, final SubstepsActionManager actionManager) {
        super(disposedChecker);
        this.infoMessageRenderer = infoMessageRenderer;
        this.testCounterRenderer = testCounterRenderer;
        this.viewTitleRenderer = viewTitleRenderer;
        this.testViewer = testViewer;
        this.testRunStats = testRunStats;
        this.actionManager = actionManager;
    }


    @Override
    public void reset() {
        // No-op
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

        final boolean hasErrorsOrFailures = testRunStats.get().hasErrorsOrFailures();
        actionManager.nextAction().setEnabled(hasErrorsOrFailures);
        actionManager.prevAction().setEnabled(hasErrorsOrFailures);
    }

}
