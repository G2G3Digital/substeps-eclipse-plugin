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
