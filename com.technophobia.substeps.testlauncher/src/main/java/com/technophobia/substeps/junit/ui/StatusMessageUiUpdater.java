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

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.technophobia.eclipse.ui.Resettable;
import com.technophobia.eclipse.ui.UiUpdater;

public class StatusMessageUiUpdater implements UiUpdater, Resettable {

    private final IViewSite viewSite;


    public StatusMessageUiUpdater(final IViewSite viewSite) {
        this.viewSite = viewSite;
    }


    @Override
    public void doUpdate() {
        // TODO Auto-generated method stub

    }


    @Override
    public void reset() {
        getStatusLine().setMessage(null);
        getStatusLine().setErrorMessage(null);
    }


    private IStatusLineManager getStatusLine() {
        // we want to show messages globally hence we
        // have to go through the active part
        final IViewSite site = viewSite;
        final IWorkbenchPage page = site.getPage();
        final IWorkbenchPart activePart = page.getActivePart();

        if (activePart instanceof IViewPart) {
            final IViewPart activeViewPart = (IViewPart) activePart;
            final IViewSite activeViewSite = activeViewPart.getViewSite();
            return activeViewSite.getActionBars().getStatusLineManager();
        }

        if (activePart instanceof IEditorPart) {
            final IEditorPart activeEditorPart = (IEditorPart) activePart;
            final IEditorActionBarContributor contributor = activeEditorPart.getEditorSite().getActionBarContributor();
            if (contributor instanceof EditorActionBarContributor)
                return ((EditorActionBarContributor) contributor).getActionBars().getStatusLineManager();
        }
        // no active part
        return viewSite.getActionBars().getStatusLineManager();
    }
}
