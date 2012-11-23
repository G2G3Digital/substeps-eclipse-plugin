/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.junit.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;

import com.technophobia.eclipse.ui.Resettable;
import com.technophobia.eclipse.ui.UiUpdater;
import com.technophobia.eclipse.ui.part.PartMonitor;
import com.technophobia.substeps.junit.ui.image.ProgressImages;
import com.technophobia.substeps.supplier.Supplier;

public class ViewTitleUiUpdater implements UiUpdater, Resettable {

    private final PartMonitor partMonitor;
    private final Supplier<SubstepsRunSession> substepsRunSession;
    private final Supplier<TestRunStats> testRunStats;

    private final Image originalViewImage;
    private Image viewImage;
    private final SubstepsIconProvider iconProvider;
    private final ProgressImages progressImages;
    private final IPropertyListener propertyListener;


    public ViewTitleUiUpdater(final PartMonitor partMonitor, final Supplier<SubstepsRunSession> substepsRunSession,
            final Supplier<TestRunStats> testRunStats, final SubstepsIconProvider iconProvider,
            final Image originalViewImage, final ProgressImages progressImages, final IPropertyListener propertyListener) {
        this.partMonitor = partMonitor;
        this.substepsRunSession = substepsRunSession;
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
        final SubstepsRunSession session = substepsRunSession.get();
        if (substepsRunSession == null || session.isStopped() || session.isRunning() || session.getStartedCount() == 0)
            viewImage = originalViewImage;
        else if (testRunStats.get().hasErrorsOrFailures())
            viewImage = iconProvider.imageFor(SubstepsTestIcon.TestRunFail);
        else
            viewImage = iconProvider.imageFor(SubstepsTestIcon.TestRunOK);
        firePropertyChange(IWorkbenchPart.PROP_TITLE);
    }


    private void updateViewTitleProgress() {
        if (substepsRunSession != null) {
            if (substepsRunSession.get().isRunning()) {
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
