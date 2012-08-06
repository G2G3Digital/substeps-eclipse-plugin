package com.technophobia.eclipse.ui.part;

import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;

public class DefaultVisibilityPartMonitor implements PartMonitor {

    private boolean visible = false;
    private final IWorkbenchPartSite site;


    public DefaultVisibilityPartMonitor(final IWorkbenchPartSite site) {
        this.site = site;
    }


    @Override
    public void partHidden(final IWorkbenchPartReference partRef) {
        if (site.getId().equals(partRef.getId())) {
            visible = false;
        }
    }


    @Override
    public void partVisible(final IWorkbenchPartReference partRef) {
        if (site.getId().equals(partRef.getId())) {
            visible = true;
        }
    }


    @Override
    public boolean isPartVisible() {
        return visible;
    }


    @Override
    public void partActivated(final IWorkbenchPartReference partRef) {
        // No-op
    }


    @Override
    public void partBroughtToTop(final IWorkbenchPartReference partRef) {
        // No-op
    }


    @Override
    public void partClosed(final IWorkbenchPartReference partRef) {
        // No-op
    }


    @Override
    public void partDeactivated(final IWorkbenchPartReference partRef) {
        // No-op
    }


    @Override
    public void partOpened(final IWorkbenchPartReference partRef) {
        // No-op
    }


    @Override
    public void partInputChanged(final IWorkbenchPartReference partRef) {
        // No-op
    }
}
