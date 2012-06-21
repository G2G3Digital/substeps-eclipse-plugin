package com.technophobia.substeps.junit.ui;

import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.ProgressImages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.Resettable;
import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.eclipse.ui.part.PartMonitor;

public class ViewTitleUiUpdater implements UiUpdater, Resettable {

    private final PartMonitor partMonitor;
    private final Supplier<TestRunSession> testRunSession;
    private final Supplier<TestRunStats> testRunStats;

    private final Image originalViewImage;
    private Image viewImage;
    private final SubstepsIconProvider iconProvider;
    private final ProgressImages progressImages;
    private final IPropertyListener propertyListener;


    public ViewTitleUiUpdater(final PartMonitor partMonitor, final Supplier<TestRunSession> testRunSession,
            final Supplier<TestRunStats> testRunStats, final SubstepsIconProvider iconProvider,
            final Image originalViewImage, final ProgressImages progressImages, final IPropertyListener propertyListener) {
        this.partMonitor = partMonitor;
        this.testRunSession = testRunSession;
        this.testRunStats = testRunStats;
        this.iconProvider = iconProvider;
        this.originalViewImage = originalViewImage;
        this.viewImage = originalViewImage;
        this.progressImages = progressImages;
        this.propertyListener = propertyListener;
    }


    @Override
    public void doUpdate() {
        if (!partMonitor.isPartVisible())
            updateViewTitleProgress();
        else {
            updateViewIcon();
        }
    }


    @Override
    public void reset() {
        resetViewIcon();
    }


    private void updateViewIcon() {
        final TestRunSession session = testRunSession.get();
        if (testRunSession == null || session.isStopped() || session.isRunning() || session.getStartedCount() == 0)
            viewImage = originalViewImage;
        else if (testRunStats.get().hasErrorsOrFailures())
            viewImage = iconProvider.imageFor(SubstepsIcon.TestRunFail);
        else
            viewImage = iconProvider.imageFor(SubstepsIcon.TestRunOK);
        firePropertyChange(IWorkbenchPart.PROP_TITLE);
    }


    private void updateViewTitleProgress() {
        if (testRunSession != null) {
            if (testRunSession.get().isRunning()) {
                final TestRunStats stats = testRunStats.get();
                final Image progress = progressImages.getImage(stats.getStartedCount(), stats.getTotalCount(),
                        stats.getErrorCount(), stats.getFailureCount());
                if (progress != viewImage) {
                    viewImage = progress;
                    firePropertyChange(IWorkbenchPart.PROP_TITLE);
                }
            } else {
                updateViewIcon();
            }
        } else {
            resetViewIcon();
        }
    }


    private void resetViewIcon() {
        viewImage = originalViewImage;
        firePropertyChange(IWorkbenchPart.PROP_TITLE);
    }


    private void firePropertyChange(final int propId) {
        propertyListener.propertyChanged(this, propId);
    }
}
